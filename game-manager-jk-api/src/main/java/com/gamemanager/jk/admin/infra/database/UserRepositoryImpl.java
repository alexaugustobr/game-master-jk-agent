package com.gamemanager.jk.admin.infra.database;

import com.gamemanager.jk.admin.domain.user.UserEntity;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
	
	private final Map<Integer, UserEntity> users = new LinkedHashMap<>();
	
	@Override
	public Optional<UserEntity> findByUserName(String userName) {
		return users.values().stream().filter(user -> Objects.equals(userName, user.getUsername())).findAny();
	}

	@Override
	public Optional<UserEntity> findBySlot(Integer slot) {
		return Optional.ofNullable(users.get(slot));
	}

	@Override
	public UserEntity save(UserEntity user) {
		users.put(user.getSlot(), user);
		return user;
	}

	@Override
	public List<UserEntity> findAll() {
		return new ArrayList<>(users.values());
	}
}
