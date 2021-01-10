package com.gamemanager.jk.admin.config;

import com.gamemanager.jk.admin.domain.server.Server;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("jk.server")
public class ServerProperties {
	
	private String ip;
	
	private int port;
	
	private String configPath;
	
	private String logPath;
	
	private String openJKLogPath;
	
	private String restartCommand;
	
	private String poweroffCommand;
	
	private String poweronCommand;
	
	private String updateCommand;
	
	private String soutCommand;
	
	private Server.Type type;
	
	private String serverHomePath;
	
	private String jampFolderPath;

}
