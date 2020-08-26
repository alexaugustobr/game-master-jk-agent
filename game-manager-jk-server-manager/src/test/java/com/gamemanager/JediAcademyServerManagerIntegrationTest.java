package com.gamemanager;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@RunWith(JUnit4.class)
@Category(IntegrationTest.class)
@Ignore
public class JediAcademyServerManagerIntegrationTest {
	
	@ClassRule
	public static DockerComposeContainer environment;
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(JediAcademyServerManagerIntegrationTest.class);

	static Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

	static {
		try {
			URI configUri = JediAcademyServerManagerIntegrationTest.class.getResource("/docker-compose.yml")
					.toURI();
			
			File configFile = new File(configUri);
			environment = new DockerComposeContainer(configFile)
					.withLocalCompose(true)
					.withLogConsumer("ffa", logConsumer);
			
			environment.start();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void dado_um_servidor_que_existe_deve_retornar_quantidade_de_jogadores() throws UnknownHostException,
			CommunicatingWithServerException {
		
		JediAcademyServerConnector connector = new JediAcademyServerConnector(
				"127.0.0.1", 29071
		);
		
		JediAcademyServerManager manager = new JediAcademyServerManager(connector);
		
		Assert.assertEquals(0, manager.asAnonymous().getPlayerCount());
		
	}
	
	@Test(expected = CommunicatingWithServerException.class)
	public void dado_um_servidor_que_nao_existe_deve_jogar_exception() throws UnknownHostException,
			CommunicatingWithServerException {
		
		JediAcademyServerConnector connector = new JediAcademyServerConnector(
				"127.0.0.8", 29072
		);
		
		JediAcademyServerManager manager = new JediAcademyServerManager(connector);
		
		manager.asAnonymous().getPlayerCount();
		
	}
	
	
	
}