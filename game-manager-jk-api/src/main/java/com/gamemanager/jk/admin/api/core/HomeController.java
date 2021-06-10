package com.gamemanager.jk.admin.api.core;

import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
	
	private final UserRepository userRepository;
	
	@GetMapping
	public void home(@AuthenticationPrincipal UserDetails userDetails) {
		System.out.println(userRepository.findAll());
	}
	
}
