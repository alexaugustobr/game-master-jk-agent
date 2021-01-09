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
		
		if (User.Type.CUSTOMER.equals(user.getType())) {
			return String.format("redirect:/%s/servers", user.getCustomer().getId());
		}
		
		if (User.Type.BUSINESS.equals(user.getType())) {
			return "redirect:/admin/servers";
		}
		
		if (User.Type.SYSTEM.equals(user.getType())) {
			//TODO EXCEPTION
			return "redirect:/logoff";
		}
		
		return "home";
	}
	
}
