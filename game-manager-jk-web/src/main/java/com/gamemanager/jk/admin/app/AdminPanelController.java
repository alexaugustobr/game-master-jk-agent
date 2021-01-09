package com.gamemanager.jk.admin.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {
	
	@GetMapping
	public String dash() {
		return "admin-panel";
	}
	
	@GetMapping("/servers")
	public String servers() {
		return "admin-panel";
	}
	
	@GetMapping("/users")
	public String users() {
		return "admin-panel";
	}
	
}
