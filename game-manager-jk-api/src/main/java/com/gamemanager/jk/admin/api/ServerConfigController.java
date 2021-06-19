package com.gamemanager.jk.admin.api;

import com.gamemanager.jk.admin.config.ConfigDataLoader;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

@Controller
@RequestMapping("/api/v1/server-config")
@AllArgsConstructor
@Slf4j
public class ServerConfigController {
	
	private final ServerRepository serverRepository;
	private final ConfigDataLoader configDataLoader;
	
	@GetMapping("/download")
	public ResponseEntity<Resource> download(@AuthenticationPrincipal UserEntity user) throws FileNotFoundException {
		
		Server server = serverRepository.loadCurrent();
		File file = new File(server.getConfigPath());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		return ResponseEntity.ok()
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
	
	@PutMapping("/upload")
	public ResponseEntity<Void> upload(MultipartFile file) {
		Server server = serverRepository.loadCurrent();
		try {
			String bkpPath = String.format(server.getConfigPath() + ".bkp-%s", OffsetDateTime.now().toString());
			log.info("Backup file created: "+ bkpPath);
			Files.copy(Paths.get(server.getConfigPath()), Paths.get(bkpPath));
			file.transferTo(server.getConfigFile());
			configDataLoader.load();
			String msg = "Restart the server to apply the config!";
			log.info(msg);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			String msg = String.format("Error when trying update the server config %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			return ResponseEntity.status(500).build();
		}
	}
	
}
