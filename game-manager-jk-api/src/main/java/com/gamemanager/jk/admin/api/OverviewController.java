package com.gamemanager.jk.admin.api;

import com.gamemanager.JediAcademyServerConnector;
import com.gamemanager.JediAcademyServerManager;
import com.gamemanager.ServerStatusType;
import com.gamemanager.jk.admin.api.server.ServerOverviewModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/server")
@AllArgsConstructor
@Slf4j
public class OverviewController {
	
	private final ServerRepository serverRepository;
	
	@GetMapping
	public ServerOverviewModel jk() {
		Server server = serverRepository.loadCurrent();
		
		try {
			JediAcademyServerConnector connector = new JediAcademyServerConnector(server.getIp(), server.getPort());

			JediAcademyServerManager jediAcademyServerManager = new JediAcademyServerManager(connector);
			
			List<String> players = jediAcademyServerManager.asAnonymous().getPLayers();
			
			Map<ServerStatusType, String> status = jediAcademyServerManager.asAnonymous().getStatus();
			
			ServerOverviewModel overview = new ServerOverviewModel();
			overview.parse(status);
			overview.setPlayers(players);
			
			overview.setIp(server.getIp());
			overview.setPort(server.getPort());
			
			return overview;
		} catch (Exception e) {
			String msg = String.format("Error when trying to connect to the server %s:%s.", 
					server.getIp(), server.getPort());
			log.error(msg, e);
			throw  new RuntimeException(msg);
		}
		
	}
	
	
}
