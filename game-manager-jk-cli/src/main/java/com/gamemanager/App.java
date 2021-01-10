package com.gamemanager;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class App {
	
	public static void main( String[] args ) throws Exception {
		JediAcademyServerManager manager = new JediAcademyServerManager(new JediAcademyServerConnector("159.69.214.101", 29071));
		
		JediAcademyServerManager.AnonymousCommandSender anonymousCommandSender = manager.asAnonymous();
		
		Map<ServerStatusType, String> detailedStatus = manager.asAnonymous().getBasicInfo();
		
		int playerCount = anonymousCommandSender.getPlayerCount();
		
		manager.asRoot("pass")
				.resetServer();

		manager.asSmod("pass")
				.changeMap("mapName");
		
		log.info("lo");

	}
}
