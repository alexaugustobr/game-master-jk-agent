package com.gamemanager.jk.admin.infra.database;

import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ServerRepositoryImpl implements ServerRepository {
	
	private Map<String, GameServerConfig> servers = new LinkedHashMap<>();
	
	@Override
	public GameServerConfig loadCurrent() {
		return servers.get(servers.keySet().iterator().next());
	}
	
	@Override
	public GameServerConfig store(GameServerConfig server) {
		servers.put(server.getName(), server);
		return server;
	}
}
