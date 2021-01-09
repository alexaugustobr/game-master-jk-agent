package com.gamemanager.jk.admin.domain.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {
	List<Server> findAllByCustomerId(UUID tenantId);
}
