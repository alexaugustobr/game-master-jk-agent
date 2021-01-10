package com.gamemanager;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@RunWith(JUnit4.class)
//@Category(Integration.class) //TODO
public class JediAcademyServerManagerIntegrationIT {
	
	@ClassRule
	public static DockerComposeContainer environment;
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JediAcademyServerManagerIntegrationIT.class);
	
	static Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
	
	static {
		try {
			environment = new DockerComposeContainer(new File(JediAcademyServerManagerIntegrationIT.class.getResource( "/docker-compose.yml" ).toURI()))
					.withLocalCompose(true)
					.withLogConsumer("ffa", logConsumer);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
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
	
	
	
}