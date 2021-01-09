package com.gamemanager.jk.admin.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/{customerId}/servers/{serverId}")
public class CustomerPanelJkController {
	
	@GetMapping
	public String jk(@PathVariable UUID customerId, @PathVariable UUID serverId) {
		return "jk-panel";
	}
	
}
