package com.gamemanager.jk.admin.app;

import com.gamemanager.JediAcademyServerConnector;
import com.gamemanager.JediAcademyServerManager;
import com.gamemanager.ServerStatusType;
import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.app.model.ServerOverviewModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/server")
@AllArgsConstructor
@Slf4j
public class OverviewController {
	
	private final ServerRepository serverRepository;
	private final UserRepository userRepository;
	
	@GetMapping
	public String jk(@AuthenticationPrincipal User user,
					 Model model,
					 RedirectAttributes attributes) {
		
		
		Server server = serverRepository.findFirst();
		
		
		
		try {
			JediAcademyServerConnector connector = new JediAcademyServerConnector(server.getIp(),
					server.getPort());

			JediAcademyServerManager jediAcademyServerManager = new JediAcademyServerManager(connector);
			
			List<String> players = jediAcademyServerManager.asAnonymous().getPLayers();
			
			//jediAcademyServerManager.
			
			Map<ServerStatusType, String> status = jediAcademyServerManager.asAnonymous().getStatus();
			
			ServerOverviewModel overview = new ServerOverviewModel();
			overview.parse(status);
			overview.setPlayers(players);
			
			model.addAttribute("overview", overview);
			model.addAttribute("error", false);
			return "overview";
		} catch (Exception e) {
			String msg = String.format("Error when trying to connect to the server %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			model.addAttribute("message", MessageModel.error(msg));
			model.addAttribute("error", true);
			return "overview";
		}
		
	}
	
	
}
