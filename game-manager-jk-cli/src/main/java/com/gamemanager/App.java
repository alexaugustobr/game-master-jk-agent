package com.gamemanager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	
	public static void main( String[] args ) throws Exception {
		JediAcademyServerManager manager = new JediAcademyServerManager(new JediAcademyServerConnector("45.137.149.2", 29070));
		
		JediAcademyServerManager.AnonymousCommandSender anonymousCommandSender = manager.asAnonymous();
		
		int playerCount = anonymousCommandSender.getPlayerCount();
		
		manager.asRoot("pass")
				.resetServer();

		manager.asSmod("pass")
				.changeMap("mapName");
		
		log.info("lo");

	}
}
