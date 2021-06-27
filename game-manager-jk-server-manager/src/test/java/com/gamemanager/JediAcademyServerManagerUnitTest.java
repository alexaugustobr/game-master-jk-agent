package com.gamemanager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class JediAcademyServerManagerUnitTest {
	
	JediAcademyServerManager manager;
	JediAcademyServerConnector connector;
	private GameServerConfig gameServerConfig;
	
	@Before
	public void init() throws Exception {
		connector = new JediAcademyServerConnector("127.0.0.1", 29070);
		manager = new JediAcademyServerManager();
		gameServerConfig = new GameServerConfig();
		gameServerConfig.setMb2Path("/home/admin/public/gamefiles/MBII");
	}
	
	@Test
	public void testAsAnonymous() throws IOException {
		JediAcademyServerManager.AnonymousCommandSender sender = manager.asAnonymous(connector);
		Assert.assertEquals(connector, sender.getConnector());
	}
	
	@Test
	public void testAsSmod() {
		
		String smodPass = "pass";
		
		JediAcademyServerManager.SmodCommandSender sender = manager.asSmod(connector, smodPass);
		
		Assert.assertEquals(connector, sender.getConnector());
		Assert.assertEquals(smodPass, sender.getPass());
	}
	
	@Test
	public void testAsRoot() {
		String pass = "pass";
		
		JediAcademyServerManager.RootCommandSender sender = manager.asRoot(connector, pass);
		
		Assert.assertEquals(connector, sender.getConnector());
		Assert.assertEquals(pass, sender.getPass());
	}
	
	@Test
	public void testListMaps() {
		List<GameMap> gameMaps = manager.runLocally(gameServerConfig).getGameMaps();
		Assert.assertFalse(gameMaps.isEmpty());
	}
}