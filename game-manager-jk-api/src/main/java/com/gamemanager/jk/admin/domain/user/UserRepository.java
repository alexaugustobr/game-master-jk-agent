package com.gamemanager.jk.admin.domain.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
	
	Optional<UserEntity> findByUserName(String userName);

	Optional<UserEntity> findBySlot(Integer slot);
	
	UserEntity save(UserEntity user);
	
	List<UserEntity> findAll();
}
