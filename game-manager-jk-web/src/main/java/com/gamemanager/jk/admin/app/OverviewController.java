package com.gamemanager.jk.admin.app;

import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/server")
@AllArgsConstructor
@Slf4j
public class OverviewController {
	
	private final ServerRepository serverRepository;
	private final UserRepository userRepository;
	
	@GetMapping
	public String jk(@AuthenticationPrincipal User user,
					 RedirectAttributes attributes) {
		
		
		Server server = serverRepository.findFirst();
		
		try {
//			JediAcademyServerConnector connector = new JediAcademyServerConnector(server.getIp(),
//					server.getPort());
//
//			JediAcademyServerManager jediAcademyServerManager = new JediAcademyServerManager(connector);
//
//			Map<ServerStatusType, String> status = jediAcademyServerManager.asAnonymous().getStatus();
//
//			//MAP_NAME
//			//G_AUTHENTICITY
//			//GAME_NAME
//			//SV_HOSTNAME
//			//G_NEED_PASS
//			//G_GAME_TYPE = 7 mb2
//
//
//
//			//NEEDPASS
//			//GAME
//			//CLIENTS
//			//MAP_NAME
//			//HOSTNAME
//
//			String playersCount = status.get(ServerStatusType.CLIENTS);
//			String maxSlots = status.get(ServerStatusType.SV_MAXCLIENTS);
//			String gameName = status.get(ServerStatusType.GAME);//MBII / JK
//			String mapName = status.get(ServerStatusType.MAP_NAME);
//
//			String gameNameAndVersion = status.get(ServerStatusType.G_NEED_PASS);// Movie Battles II V1.7.1
//			String authenticity = status.get(ServerStatusType.G_AUTHENTICITY);// Movie Battles II V1.7.1
//
			return "overview";
		} catch (Exception e) {
			String msg = String.format("Error when trying to connect to the server %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.error(msg));
			return "redirect:/500";
		}
		
	}
	
}
