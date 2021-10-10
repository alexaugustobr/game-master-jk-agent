package com.gamemanager.jk.admin.api.log;

import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.api.TailMessage;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@AllArgsConstructor
@Slf4j
public class LogController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping("/game")
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

	@GetMapping("/game/{fileName}")
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

	@GetMapping("/game/tail")
	public List<TailMessage> gameLogTail() throws Exception {
		List<TailMessage> tailMessages = new ArrayList<>();
		GameServerConfig server = serverRepository.loadCurrent();

		try {
			String msg = "Server tail logs command send!";
			log.info(msg);

			CommandLine cmdLine = CommandLine.parse(server.getTailLogCommand());
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

			ExecuteWatchdog watchdog = new ExecuteWatchdog(Duration.ofSeconds(2).toMillis());
			Executor executorWithStdout = new DefaultExecutor();

			ByteArrayOutputStream stdout = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(stdout);

			executorWithStdout.setExitValue(0);
			executorWithStdout.setStreamHandler(streamHandler);
			executorWithStdout.setWatchdog(watchdog);
			executorWithStdout.execute(cmdLine, resultHandler);

			String retval;
			try {
				resultHandler.waitFor();
				int exitCode = resultHandler.getExitValue();
				retval = StringUtils.chomp(stdout.toString());


				if (resultHandler.getException() != null) {
					log.warn("{}", resultHandler.getException().getMessage());
				} else {

					final String[] lines = retval.split("\n");

					for (String line : lines) {
						final int indexOfZ = line.indexOf("Z");
						final var rawDate = line.substring(0, indexOfZ+1);
						final var rawMessage = line.substring(indexOfZ+1).trim();

						if (rawMessage.isBlank() || rawMessage.isEmpty()) {
							continue;
						}

						tailMessages.add(
								new TailMessage(
										OffsetDateTime.parse(rawDate),
										rawMessage
								)
						);

					}

					//log.info("exit code '{}', result '{}'", exitCode, retval);
				}
			} catch (InterruptedException e) {
				log.error("Timeout occurred when executing commandLine '{}'", cmdLine, e);
			}

		} catch (Exception e) {
			String msg = "Error occurred when sending the server tail command to the server!";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}

		Collections.reverse(tailMessages);

		return tailMessages;
	}
	
	
}
