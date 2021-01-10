package com.gamemanager.jk.admin.domain.server;

import org.springframework.stereotype.Repository;

@Repository
public interface SigleServerRepository {
	
	Server findFirst();
	
	Server save(Server server);
	
}
