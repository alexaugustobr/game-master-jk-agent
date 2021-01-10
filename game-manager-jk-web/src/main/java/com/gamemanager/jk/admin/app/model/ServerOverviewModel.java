package com.gamemanager.jk.admin.app.model;

import com.gamemanager.ServerStatusType;
import com.gamemanager.jk.admin.domain.server.NameUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerOverviewModel {
	
	private String serverName;
	private String playersCount;
	private String maxSlots;
	private String gameName;
	private String mapName;
	private String gameNameAndVersion;
	private String authenticity;
	private List<String> players = new ArrayList<>();
	
	public String getServerNameWithoutColors() {
		return NameUtils.removeColorTags(serverName);
	}
	
	public List<String> getPlayersWithoutColors() {
		return players.stream().map(NameUtils::removeColorTags).collect(Collectors.toList());
	}
	
	public void parse(Map<ServerStatusType, String> status) {
		this.playersCount = status.get(ServerStatusType.CLIENTS);
		this.serverName = status.get(ServerStatusType.HOSTNAME);
		this.maxSlots = status.get(ServerStatusType.SV_MAXCLIENTS);
		this.gameName = status.get(ServerStatusType.GAME);//MBII / JK
		this.mapName = status.get(ServerStatusType.MAP_NAME);
		this.gameNameAndVersion = status.get(ServerStatusType.G_NEED_PASS);// Movie Battles II V1.7.1
		this.authenticity = status.get(ServerStatusType.G_AUTHENTICITY);// Movie Battles II V1.7.1
	}
	
}
