package com.gamemanager.jk.admin.config;

import com.gamemanager.GameServerConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("jk.server")
@Validated
@Data
public class ServerProperties {
	
	private String ip;
	private int port;
	private String configPath;
	private String logPath;
	private String openJKLogPath;
	private String zipLogPath;
	private String restartCommand;
	private String poweroffCommand;
	private String poweronCommand;
	private String updateCommand;
	private String tailLogCommand;
	private String tailRtvLogCommand;
	private GameServerConfig.Type type;
	private String serverHomePath;
	private String jampPath;
	private String mb2Path;
	private String rtvPath;
	private String rtvRestartCommand;
	
}
