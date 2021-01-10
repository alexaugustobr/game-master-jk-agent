package com.gamemanager.jk.admin.domain.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Server {
	
	private String name;
	
	private String ip;
	
	private int port;
	
	private String rconPass;
	
	private String configPath;
	
	private String logPath;
	
	private String openJKLogPath;
	
	private String restartCommand;
	
	private String soutCommand;
	
	private Type type;
	
	private String serverHomePath;
	
	private String jampFolderPath;
	
	public enum Type {
		JAMP,
		OPENJK
	}
}
