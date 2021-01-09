package com.gamemanager.jk.admin.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
	
	@Bean
	@NotNull
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
