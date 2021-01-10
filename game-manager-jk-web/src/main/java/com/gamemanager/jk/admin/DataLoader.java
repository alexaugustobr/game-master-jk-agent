package com.gamemanager.jk.admin;

import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.SigleServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.gamemanager.jk.admin.domain.server.Server.Type.OPENJK;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final SigleServerRepository sigleServerRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//TODO CONFIG PARSERlo
		userRepository.save(User.builder()
				.role(User.Role.SYSTEM)
				.userName("rcon")
				.password("#fuckdevs")
				.enabled(true)
				.build());
		
		sigleServerRepository.save(Server.builder()
				.ip("159.69.214.101")
				.port(29071)
				.rconPass("#fuckdevs")
				.restartCommand("/home/admin/servers/restart.sh")
				.type(OPENJK)
				.logPath("/home/admin/servers/jamp/mbii-duel/MBII/server.log")
				.openJKLogPath("/home/admin/servers/jamp/mbii-duel/MBII/openjk_server.log")
				.configPath("/home/admin/servers/jamp/mbii-duel/MBII/server.cfg")
				.serverHomePath("/home/admin/servers/jamp/mbii-duel")
				.jampFolderPath("/home/admin/volumes/jamp")
				.build());
	}
}
