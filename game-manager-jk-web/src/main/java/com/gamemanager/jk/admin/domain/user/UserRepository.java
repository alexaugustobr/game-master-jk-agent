package com.gamemanager.jk.admin.domain.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
	
	Optional<User> findByUserName(String userName);
	
	User save(User user);
	
}
