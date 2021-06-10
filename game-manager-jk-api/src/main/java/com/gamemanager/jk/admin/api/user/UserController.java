package com.gamemanager.jk.admin.api.user;

import com.gamemanager.jk.admin.domain.user.UserEntity;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import com.gamemanager.jk.admin.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
	
	private final UserRepository users;
	private final ModelMapper modelMapper;
	
	@GetMapping
	public List<UserModel> findAll() {
		return users.findAll()
				.stream()
				.map(user -> modelMapper.map(user, UserModel.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/{slot}")
	public UserModel findBySlot(@PathVariable Integer slot) {
		UserEntity user = users.findBySlot(slot).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(user, UserModel.class);
	}
	
	@PutMapping("/{slot}")
	public void update(@PathVariable Integer slot, 
	                   @RequestBody UserUpdateModel userUpdateModel) {
		UserEntity user = users.findBySlot(slot).orElseThrow(EntityNotFoundException::new);
		modelMapper.map(userUpdateModel, user);
		users.save(user);
	}

	@PutMapping("/{slot}/password")
	public void updatePassword(@PathVariable Integer slot,
	                   @RequestBody UserPasswordUpdateModel userPasswordUpdateModel) {
		UserEntity user = users.findBySlot(slot).orElseThrow(EntityNotFoundException::new);
		modelMapper.map(userPasswordUpdateModel, user);
		users.save(user);
	}

	@PutMapping("/{slot}/enable")
	public void enable(@PathVariable Integer slot) {
		UserEntity user = users.findBySlot(slot).orElseThrow(EntityNotFoundException::new);
		user.setPassword("123"); //TODO revert last pass and permission
		user.setEnabled(true);
		users.save(user);
	}

	@DeleteMapping("/{slot}/enable")
	public void disable(@PathVariable Integer slot) {
		UserEntity user = users.findBySlot(slot).orElseThrow(EntityNotFoundException::new);
		user.setPassword("");
		user.setEnabled(false);
		users.save(user);
	}
	
}
