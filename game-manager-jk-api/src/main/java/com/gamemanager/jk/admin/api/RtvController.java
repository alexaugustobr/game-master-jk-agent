package com.gamemanager.jk.admin.api;

import com.gamemanager.jk.admin.api.core.MessageModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

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
		Server server = serverRepository.loadCurrent();
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
		
		Server server = serverRepository.loadCurrent();
		File file = Paths.get(server.getRtvPath(), "rtvrtm.cfg").toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@GetMapping("/maps.txt")
	public ResponseEntity<Resource> downloadPrimaryMapList(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		Server server = serverRepository.loadCurrent();
		File file = Paths.get(server.getRtvPath(), "maps.txt").toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@GetMapping("/secondary_maps.txt")
	public ResponseEntity<Resource> downloadSecondaryMapList(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		Server server = serverRepository.loadCurrent();
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
		Server server = serverRepository.loadCurrent();
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
		Server server = serverRepository.loadCurrent();
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
		Server server = serverRepository.loadCurrent();
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
	
	private void executeCommand(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		executor.execute(cmdLine, resultHandler);
	}
	
}
