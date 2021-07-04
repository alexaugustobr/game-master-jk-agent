package com.gamemanager.jk.admin.api;

import com.gamemanager.GameFile;
import com.gamemanager.GameServerConfig;
import com.gamemanager.JediAcademyServerManager;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game-files")
@AllArgsConstructor
public class GameFileController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping
	public List<GameFile> listMaps() {
		GameServerConfig config = serverRepository.loadCurrent();
		JediAcademyServerManager serverManager = new JediAcademyServerManager();
		return serverManager.runLocally(config).getGameFiles();
	}
	
	@DeleteMapping("/{fileName}")
	public void deleteMap(@PathVariable String fileName) {
		GameServerConfig config = serverRepository.loadCurrent();
		JediAcademyServerManager serverManager = new JediAcademyServerManager();
		serverManager.runLocally(config).deleteFileByName(fileName);
	}

	@PostMapping
	public ResponseEntity<Void> upload(MultipartFile file) {
		try {
			GameServerConfig server = serverRepository.loadCurrent();
			Path fileSavePath = Paths.get(server.getMb2Path(), file.getOriginalFilename());
			file.transferTo(fileSavePath.toFile());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}
}
