package com.gamemanager.jk.admin.api.log;

import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logs")
@AllArgsConstructor
public class LogController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping("/games")
	public List<GameLogModel> gameLost() {
		Server server = serverRepository.loadCurrent();
		Path logPath = Paths.get(server.getLogPath());
		String zipLogPath = server.getZipLogPath();

		List<GameLogModel> gameLogModels = new ArrayList<>();

		gameLogModels.add(new GameLogModel(
				logPath.getFileName().toString(),
				OffsetDateTime.now()
		));
		
		return gameLogModels;
	}
	
}
