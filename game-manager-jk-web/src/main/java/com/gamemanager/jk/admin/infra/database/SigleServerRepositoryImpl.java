package com.gamemanager.jk.admin.infra.database;

import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.SigleServerRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SigleServerRepositoryImpl implements SigleServerRepository {
	
	private Map<String, Server> servers = new LinkedHashMap<>();
	
	@Override
	public Server findFirst() {
		return servers.get(servers.keySet().iterator().next());
	}
	
	@Override
	public Server save(Server server) {
		return servers.put(server.getName(), server);
	}
}
