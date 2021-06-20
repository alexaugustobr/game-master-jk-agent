package com.gamemanager.jk.admin.api;

import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/maps")
@AllArgsConstructor
public class GameMapController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping
	public List<GameMapModel> listMaps() {
		Server server = serverRepository.loadCurrent();
		Path mb2Path = Paths.get(server.getJampFolderPath(), "MBII");
		
		File[] logTarFileList = mb2Path.toFile().listFiles((dir, name) -> name.toLowerCase().endsWith(".pk3"));
		List<GameMapModel> gameMapModels = new ArrayList<>();
		
		if (logTarFileList != null) {
			for (File file : logTarFileList) {
				try {
					FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
					gameMapModels.add(new GameMapModel(
							file.getName(),
							OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault())
					));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return gameMapModels;
	}

	@PostMapping
	public ResponseEntity<Void> upload(MultipartFile file) {
		try {
			Server server = serverRepository.loadCurrent();
			Path fileSavePath = Paths.get(server.getJampFolderPath(), "MBII", file.getOriginalFilename());
			file.transferTo(fileSavePath.toFile());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}
}
