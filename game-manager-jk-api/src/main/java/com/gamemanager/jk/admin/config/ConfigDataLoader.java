package com.gamemanager.jk.admin.config;

import com.gamemanager.ConfigManager;
import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.UserEntity;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigDataLoader implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final ServerRepository serverRepository;
	private final ServerProperties properties;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.warn("Loading properties");
		System.out.println("Loading properties");
		load();
	}
	
	public void load() throws IOException {
		String configPath = properties.getConfigPath();
		log.warn("Properties = " + properties);
		System.out.println(properties);
		ConfigManager configManager = new ConfigManager(configPath);
		
		String rcon = configManager.getConfig(ConfigManager.ConfigKey.RCON_PASSWORD).orElse("rcon");
		String server = configManager.getConfig(ConfigManager.ConfigKey.SV_HOSTNAME).orElse("MB2 Server");
		
		serverRepository.store(GameServerConfig.builder()
				.ip(properties.getIp())
				.port(properties.getPort())
				.type(properties.getType())
				.logPath(properties.getLogPath())
				.openJKLogPath(properties.getOpenJKLogPath())
				.zipLogPath(properties.getZipLogPath())
				.configPath(configPath)
				.serverHomePath(properties.getServerHomePath())
				.jampPath(properties.getJampPath())
				.mb2Path(properties.getMb2Path())
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

//		userRepository.save(UserEntity.builder()
//				.slot(1)
//				.type(UserEntity.Type.SMOD)
//				.userName("smod1")
//				.password(rcon)
//				.enabled(true)
//				.build());
	}
}
