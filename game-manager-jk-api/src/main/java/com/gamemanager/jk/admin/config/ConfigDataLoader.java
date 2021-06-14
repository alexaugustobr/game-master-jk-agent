package com.gamemanager.jk.admin.config;

import com.gamemanager.ConfigManager;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.UserEntity;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ConfigDataLoader implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final ServerRepository serverRepository;
	private final ServerProperties properties;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		load();
	}
	
	public void load() throws IOException {
		ConfigManager configManager = new ConfigManager(properties.getConfigPath());
		
		String rcon = configManager.getConfig(ConfigManager.ConfigKey.RCON_PASSWORD).orElse("rcon");
		String server = configManager.getConfig(ConfigManager.ConfigKey.SV_HOSTNAME).orElse("MB2 Server");
		
		serverRepository.store(Server.builder()
				.ip(properties.getIp())
				.port(properties.getPort())
				.type(properties.getType())
				.logPath(properties.getLogPath())
				.openJKLogPath(properties.getOpenJKLogPath())
				.zipLogPath(properties.getZipLogPath())
				.configPath(properties.getConfigPath())
				.serverHomePath(properties.getServerHomePath())
				.jampFolderPath(properties.getJampFolderPath())
				.restartCommand(properties.getRestartCommand())
				.poweroffCommand(properties.getPoweroffCommand())
				.poweronCommand(properties.getPoweronCommand())
				.updateCommand(properties.getUpdateCommand())
				.rtvPath(properties.getRtvPath())
				.rtvRestartCommand(properties.getRtvRestartCommand())
				.name(server)
				.rconPass(rcon)
				.build());
		
		userRepository.save(UserEntity.builder()
				.slot(0)
				.type(UserEntity.Type.RCON)
				.userName("rcon")
				.password(rcon)
				.enabled(true)
				.build());

		userRepository.save(UserEntity.builder()
				.slot(1)
				.type(UserEntity.Type.SMOD)
				.userName("smod1")
				.password(rcon)
				.enabled(true)
				.build());
	}
}
