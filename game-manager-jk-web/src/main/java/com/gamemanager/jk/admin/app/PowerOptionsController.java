package com.gamemanager.jk.admin.app;

import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/server/power-options")
@Slf4j
@AllArgsConstructor
public class PowerOptionsController {
	
	private final ServerRepository serverRepository;
	private final DefaultExecutor executor = new DefaultExecutor();
	
	@GetMapping
	public String powerOptions(Model model) {
		return "power-options";
	}
	
	@GetMapping("poweron")
	public String poweron(@AuthenticationPrincipal User user,
						  RedirectAttributes attributes) {
		
		Server server = serverRepository.findFirst();
		
		try {
			executeCommand(server.getPoweronCommand());
			
			String msg = "Power on command send!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			
		} catch (Exception e) {
			String msg = "Error occured when sending the power on command to the server!";
			log.error(msg);
			attributes.addFlashAttribute("message", MessageModel.error(msg));
		}
		
		return "redirect:/server/power-options";
	}
	
	@GetMapping("poweroff")
	public String poweroff(@AuthenticationPrincipal User user,
						   RedirectAttributes attributes) {
		
		Server server = serverRepository.findFirst();
		
		try {
			executeCommand(server.getPoweroffCommand());
			
			String msg = "Power off command send!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			
		} catch (Exception e) {
			String msg = "Error occured when sending the power on command to the server!";
			log.error(msg);
			attributes.addFlashAttribute("message", MessageModel.error(msg));
		}
		
		return "redirect:/server/power-options";
	}
	
	@GetMapping("restart")
	public String restart(@AuthenticationPrincipal User user,
						  RedirectAttributes attributes) {
		
		Server server = serverRepository.findFirst();
		
		try {
			executeCommand(server.getRestartCommand());
			
			String msg = "Restart command send!";
			log.info(msg);
			attributes.addFlashAttribute("message", MessageModel.success(msg));
			
		} catch (Exception e) {
			String msg = "Error occured when sending the power on command to the server!";
			log.error(msg);
			attributes.addFlashAttribute("message", MessageModel.error(msg));
		}
		
		return "redirect:/server/power-options";
	}
	
	private void executeCommand(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		executor.execute(cmdLine, resultHandler);
	}
	
}
