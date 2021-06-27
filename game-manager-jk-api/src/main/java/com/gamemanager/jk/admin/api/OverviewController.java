package com.gamemanager.jk.admin.api;

import com.gamemanager.JediAcademyServerConnector;
import com.gamemanager.JediAcademyServerManager;
import com.gamemanager.ServerStatusType;
import com.gamemanager.jk.admin.api.server.GameServerModel;
import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/server")
@AllArgsConstructor
@Slf4j
public class OverviewController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping
	public GameServerModel load() {
		GameServerConfig server = serverRepository.loadCurrent();
		
		try {
			JediAcademyServerManager jediAcademyServerManager = new JediAcademyServerManager();
			JediAcademyServerConnector connector = new JediAcademyServerConnector(server.getIp(), server.getPort());
			
			List<String> players = jediAcademyServerManager.asAnonymous(connector).getPLayers();
			Map<ServerStatusType, String> status = jediAcademyServerManager.asAnonymous(connector).getStatus();
			
			GameServerModel overview = new GameServerModel();
			overview.parse(status);
			overview.setPlayers(players);
			
			overview.setIp(server.getIp());
			overview.setPort(server.getPort());
			
			return overview;
		} catch (Exception e) {
			String msg = String.format("Error when trying to connect to the game server %s:%s.", 
					server.getIp(), server.getPort());
			log.error(msg, e);
			throw  new RuntimeException(msg);
		}
		
	}
	
	
}
