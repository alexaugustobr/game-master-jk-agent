package com.gamemanager;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class App {
	
	public static void main( String[] args ) throws Exception {
		JediAcademyServerManager manager = new JediAcademyServerManager(new JediAcademyServerConnector("116.203.157.129", 29070));
		
		JediAcademyServerManager.AnonymousCommandSender anonymousCommandSender = manager.asAnonymous();
		
		Map<ServerStatusType, String> detailedStatus = manager.asAnonymous().getDetailedInfo();

		List<String> pLayers = anonymousCommandSender.getPLayers();

		manager.asRoot("pass")
				.resetServer();

		manager.asSmod("pass")
				.changeMap("mapName");
		
		log.info("lo");

	}
}
