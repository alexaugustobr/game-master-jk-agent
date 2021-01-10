package com.gamemanager.jk.admin.infra.database;

import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
	
	private final Map<String, User> users = new LinkedHashMap<>();
	
	@Override
	public Optional<User> findByUserName(String userName) {
		return Optional.ofNullable(users.get(userName));
	}
	
	@Override
	public User save(User user) {
		users.put(user.getUsername(), user);
		return user;
	}
}
