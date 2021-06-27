package com.gamemanager.jk.admin.domain.server;

import com.gamemanager.GameServerConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository {
	
	GameServerConfig loadCurrent();
	
	GameServerConfig store(GameServerConfig server);
	
}
