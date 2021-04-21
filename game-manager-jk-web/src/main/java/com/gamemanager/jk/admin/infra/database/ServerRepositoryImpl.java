package com.gamemanager.jk.admin.infra.database;

import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ServerRepositoryImpl implements ServerRepository {
	
	private Map<String, Server> servers = new LinkedHashMap<>();
	
	@Override
	public Server loadCurrent() {
		return servers.get(servers.keySet().iterator().next());
	}
	
	@Override
	public Server store(Server server) {
		servers.put(server.getName(), server);
		return server;
	}
}
