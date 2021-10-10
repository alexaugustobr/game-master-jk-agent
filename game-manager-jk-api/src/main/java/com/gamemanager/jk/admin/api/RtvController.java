package com.gamemanager.jk.admin.api;

import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.api.core.MessageModel;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.UserEntity;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/plugins/rtv")
@AllArgsConstructor
@Slf4j
public class RtvController {
	
	private final ServerRepository serverRepository;
	private final DefaultExecutor executor = new DefaultExecutor();
	
	@GetMapping
	public String configUpload(@AuthenticationPrincipal UserEntity user) {
		return "rtv";
	}
	
	@GetMapping("/restart")
	public String restart(@AuthenticationPrincipal UserEntity user,  RedirectAttributes attributes) {
		GameServerConfig server = serverRepository.loadCurrent();
		try {
			executeCommand(server.getRtvRestartCommand());
			String msg = "RTV is restarting!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			return "redirect:/server/rtv";
		} catch (Exception e) {
			String msg = String.format("Error when trying to restart RTV %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.danger(msg));
			return "redirect:/server/rtv";
		}
	}
	
	@GetMapping("/rtvrtm.cfg")
	public ResponseEntity<Resource> downloadRtv(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		GameServerConfig server = serverRepository.loadCurrent();
		File file = Paths.get(server.getRtvPath(), "rtvrtm.cfg").toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@GetMapping("/maps.txt")
	public ResponseEntity<Resource> downloadPrimaryMapList(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		GameServerConfig server = serverRepository.loadCurrent();
		File file = Paths.get(server.getRtvPath(), "maps.txt").toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@GetMapping("/secondary_maps.txt")
	public ResponseEntity<Resource> downloadSecondaryMapList(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		GameServerConfig server = serverRepository.loadCurrent();
		File file = Paths.get(server.getRtvPath(), "secondary_maps.txt").toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@PostMapping("/rtvrtm")
	public String uploadConfig(@AuthenticationPrincipal UserEntity user,
						 RedirectAttributes attributes,
						 MultipartFile file) {
		GameServerConfig server = serverRepository.loadCurrent();
		try {
			String bkpPath = String.format(server.getConfigPath() + ".bkp-%s", OffsetDateTime.now().toString());
			log.info("Backup file created: "+ bkpPath);
			Files.copy(Paths.get(server.getRtvPath(), "rtvrtm.cfg"), Paths.get(bkpPath));
			file.transferTo(Paths.get(server.getRtvPath(), "rtvrtm.cfg").toFile());
			//configDataLoader.load();
			String msg = "Restart RTV to apply the config!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			return "redirect:/server/rtv";
		} catch (Exception e) {
			String msg = String.format("Error when trying update the RTV config %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.danger(msg));
			return "redirect:/server/rtv";
		}
	}
	
	@PostMapping("/primary-maps")
	public String uploadPrimaryMaps(@AuthenticationPrincipal UserEntity user,
							   RedirectAttributes attributes,
							   MultipartFile file) {
		GameServerConfig server = serverRepository.loadCurrent();
		try {
			String bkpPath = String.format(server.getConfigPath() + ".bkp-%s", OffsetDateTime.now().toString());
			log.info("Backup file created: "+ bkpPath);
			Files.copy(Paths.get(server.getRtvPath(), "maps.txt"), Paths.get(bkpPath));
			file.transferTo(Paths.get(server.getRtvPath(), "maps.txt").toFile());
			//configDataLoader.load();
			String msg = "Restart RTV to apply the config!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			return "redirect:/server/rtv";
		} catch (Exception e) {
			String msg = String.format("Error when trying update the primary map list %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.danger(msg));
			return "redirect:/server/rtv";
		}
	}
	
	@PostMapping("/secondary-maps")
	public String uploadSecondaryMaps(@AuthenticationPrincipal UserEntity user,
							   RedirectAttributes attributes,
							   MultipartFile file) {
		GameServerConfig server = serverRepository.loadCurrent();
		try {
			String bkpPath = String.format(server.getConfigPath() + ".bkp-%s", OffsetDateTime.now().toString());
			log.info("Backup file created: "+ bkpPath);
			Files.copy(Paths.get(server.getRtvPath(), "secondary_maps.txt"), Paths.get(bkpPath));
			file.transferTo(Paths.get(server.getRtvPath(), "secondary_maps.txt").toFile());
			//configDataLoader.load();
			String msg = "Restart RTV to apply the config!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			return "redirect:/server/rtv";
		} catch (Exception e) {
			String msg = String.format("Error when trying update the secondary map list %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.danger(msg));
			return "redirect:/server/rtv";
		}
	}

	@GetMapping("/log-tail")
	public List<TailMessage> logTail() throws Exception {
		List<TailMessage> tailMessages = new ArrayList<>();
		GameServerConfig server = serverRepository.loadCurrent();

		try {
			String msg = "Rtv tail logs command send!";
			log.info(msg);
			
			CommandLine cmdLine = CommandLine.parse(server.getTailRtvLogCommand());
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
			String msg = "Error occurred when sending the rtv tail command to the server!";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}

		Collections.reverse(tailMessages);
		
		return tailMessages;
	}
	
	private void executeCommand(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		executor.execute(cmdLine, resultHandler);
	}
	
}
