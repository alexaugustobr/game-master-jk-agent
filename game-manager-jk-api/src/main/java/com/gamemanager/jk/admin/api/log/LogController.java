package com.gamemanager.jk.admin.api.log;

import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@AllArgsConstructor
public class LogController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping("/games")
	public List<GameLogFileModel> listGameLogs() {
		GameServerConfig server = serverRepository.loadCurrent();
		Path logPath = Paths.get(server.getLogPath());
		File logFolder = new File(server.getZipLogPath());

		List<GameLogFileModel> gameLogFileModels = new ArrayList<>();

		try {
			FileTime creationTime = (FileTime) Files.getAttribute(logPath, "creationTime");
			gameLogFileModels.add(new GameLogFileModel(
					logPath.getFileName().toString(),
					OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault())
			));
		} catch (IOException e) {
			e.printStackTrace();
		}

		File[] logTarFileList = logFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".tar.gz"));

		if (logTarFileList != null) {
			for (File tarFile : logTarFileList) {
				try {
					FileTime creationTime = (FileTime) Files.getAttribute(tarFile.toPath(), "creationTime");
					gameLogFileModels.add(new GameLogFileModel(
							tarFile.getName(),
							OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault())
					));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return gameLogFileModels;
	}

	@GetMapping("/games/{fileName}")
	public ResponseEntity<Resource> downloadGameLog(@PathVariable String fileName) throws IOException {
		GameServerConfig server = serverRepository.loadCurrent();
		File file = Paths.get(server.getZipLogPath(), fileName).toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		String mimeType= URLConnection.guessContentTypeFromStream(new FileInputStream(file));
		MediaType mediaType;
		if (mimeType != null) {
			mediaType = MediaType.parseMediaType(mimeType);
		} else {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(mediaType)
				.body(resource);
	}
	
}
