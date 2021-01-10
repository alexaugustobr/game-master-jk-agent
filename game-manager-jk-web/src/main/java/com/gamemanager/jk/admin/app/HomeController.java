package com.gamemanager.jk.admin.app;

import com.gamemanager.jk.admin.domain.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping
	public String home(@AuthenticationPrincipal User user) {
		//TODO
		// if user and onlyOneTenant, redirect to admin
		
		if (user == null) {
			return "redirect:/login";
		}
		
		return "redirect:/server";
	}
	
}
