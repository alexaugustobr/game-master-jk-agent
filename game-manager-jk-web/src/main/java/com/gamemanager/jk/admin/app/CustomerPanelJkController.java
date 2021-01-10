package com.gamemanager.jk.admin.app;

import com.gamemanager.JediAcademyServerConnector;
import com.gamemanager.JediAcademyServerManager;
import com.gamemanager.ServerStatusType;
import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import com.gamemanager.jk.admin.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/{customerId}/servers/{serverId}")
@AllArgsConstructor
@Slf4j
public class CustomerPanelJkController {
	
	private final ServerRepository serverRepository;
	private final UserRepository userRepository;
	
	@GetMapping
	public String jk(@PathVariable UUID customerId,
					 @PathVariable UUID serverId,
					 @AuthenticationPrincipal User user,
					 RedirectAttributes attributes) {
		
		
		Server server = serverRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
		
		try {
			JediAcademyServerConnector connector = new JediAcademyServerConnector(server.getIp(),
					Integer.parseInt(server.getPort()));
			
			JediAcademyServerManager jediAcademyServerManager = new JediAcademyServerManager(connector);
			
			Map<ServerStatusType, String> status = jediAcademyServerManager.asAnonymous().getStatus();
			
			//MAP_NAME
			//G_AUTHENTICITY
			//GAME_NAME
			//SV_HOSTNAME
			//G_NEED_PASS
			//G_GAME_TYPE = 7 mb2
			
			
			
			//NEEDPASS
			//GAME
			//CLIENTS
			//MAP_NAME
			//HOSTNAME
			
			String playersCount = status.get(ServerStatusType.CLIENTS);
			String maxSlots = status.get(ServerStatusType.SV_MAXCLIENTS);
			String gameName = status.get(ServerStatusType.GAME);//MBII / JK
			String mapName = status.get(ServerStatusType.MAP_NAME);
			
			String gameNameAndVersion = status.get(ServerStatusType.G_NEED_PASS);// Movie Battles II V1.7.1
			String authenticity = status.get(ServerStatusType.G_AUTHENTICITY);// Movie Battles II V1.7.1
			return "jk-panel";
		} catch (Exception e) {
			String msg = String.format("Error when trying to connect to the server %s:%s.", server.getIp(), server.getPort());
			log.error(msg, e);
			attributes.addFlashAttribute("message", MessageModel.error(msg));
			return String.format("redirect:/%s/servers", customerId);
		}
		
	}
	
}
