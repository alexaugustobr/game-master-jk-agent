package com.gamemanager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class JediAcademyServerManagerUnitTest {
	
	JediAcademyServerManager manager;
	JediAcademyServerConnector connector;
	
	@Before
	public void init() {
		connector = Mockito.mock(JediAcademyServerConnector.class);
		manager = new JediAcademyServerManager(connector);
	}
	
	@Test
	public void testAsAnonymous() throws IOException {
		JediAcademyServerManager.AnonymousCommandSender sender = manager.asAnonymous();
		Assert.assertEquals(connector, sender.getConnector());
	}
	
	@Test
	public void testAsSmod() {
		
		String smodPass = "pass";
		
		JediAcademyServerManager.SmodCommandSender sender = manager.asSmod(smodPass);
		
		Assert.assertEquals(connector, sender.getConnector());
		Assert.assertEquals(smodPass, sender.getPass());
	}
	
	@Test
	public void testAsRoot() {
		String pass = "pass";
		
		JediAcademyServerManager.RootCommandSender sender = manager.asRoot(pass);
		
		Assert.assertEquals(connector, sender.getConnector());
		Assert.assertEquals(pass, sender.getPass());
	}
}