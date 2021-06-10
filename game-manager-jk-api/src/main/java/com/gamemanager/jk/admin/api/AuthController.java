package com.gamemanager.jk.admin.api;


import com.gamemanager.jk.admin.api.user.UserModel;
import com.gamemanager.jk.admin.domain.user.UserEntity;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {
	
	private final UserRepository users;
	private final ModelMapper modelMapper;
	
	@PostMapping("/api/auth")
	public UserModel login(@RequestBody LoginForm loginForm) {
		UserEntity userEntity = users.findByUserName(loginForm.getUsername())
				.orElseThrow(() -> new AccessDeniedException("Wrong username or password"));
		if (!userEntity.getPassword().equals(loginForm.getPassword())) {
			throw new AccessDeniedException("Wrong username or password");
		}
		return modelMapper.map(userEntity, UserModel.class);
	}
	
}
