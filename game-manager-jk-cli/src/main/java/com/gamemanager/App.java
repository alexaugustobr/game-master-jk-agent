package com.gamemanager;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class App {
	
	public static void main( String[] args ) throws Exception {
		JediAcademyServerConnector jediAcademyServerConnector = new JediAcademyServerConnector("116.203.157.129", 29070);
		JediAcademyServerManager manager = new JediAcademyServerManager();
		
		JediAcademyServerManager.AnonymousCommandSender anonymousCommandSender = manager.asAnonymous(jediAcademyServerConnector);
		
		Map<ServerStatusType, String> detailedStatus = manager.asAnonymous(jediAcademyServerConnector).getDetailedInfo();

		List<String> pLayers = anonymousCommandSender.getPLayers();

		manager.asRoot(jediAcademyServerConnector, "pass")
				.resetServer();

		manager.asSmod(jediAcademyServerConnector, "pass")
				.changeMap("mapName");
		
		log.info("lo");

	}
}
