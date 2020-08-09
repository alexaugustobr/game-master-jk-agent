package com.gamemanager;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ServerStatusType {
	
	
	/* Basic info*/
	
	GAME("game"),
	FDISABLE("fdisable"),
	WDISABLE("wdisable"),
	TRUEJEDI("truejedi"),
	NEEDPASS("needpass"),
	GAMETYPE("gametype"),
	SV_MAXCLIENTS("sv_maxclients"),
	CLIENTS("clients"),
	MAP_NAME("mapname"),
	HOSTNAME("hostname"),
	PROTOCOL("protocol"),
	
	/* Detailed status */
	
	G_DUEL_FRIENDLY_TEAM("g_DuelFriendlyTeam"),
	G_DUEL_TIME_LIMIT("g_DuelTimeLimit"),
	G_JEDI_VMERC("g_jediVmerc"),
	G_HIDE_HUD_FROM_SPECS("g_HideHUDFromSpecs"),
	G_NO_SPEC_MOVE("g_noSpecMove"),
	G_TK_POINTS_KICK_COUNT("g_TKPointsKickCount"),
	G_TK_POINTS_SPEC_COUNT("g_TKPointsSpecCount"),
	G_ANTI_CHEAT("g_AntiCheat"),
	G_AUTHENTICITY("g_Authenticity"),
	G_SHUFFLE_TIMER("g_ShuffleTimer"),
	SV_FLOOD_PROTECT("sv_floodProtect"),
	SV_MAX_PING("sv_maxPing"),
	SV_MIN_PING("sv_minPing"),
	SV_MAX_RATE("sv_maxRate"),
	SV_MAX_CLIENTS("sv_maxclients"),
	SV_HOSTNAME("sv_hostname"),
	G_GAME_TYPE("g_gametype"),
	TIME_LIMIT("timelimit"),
	FRAG_LIMIT("fraglimit"),
	VERSION("version"),
	DM_FLAGS("dmflags"),
	CAPTURE_LIMIT("capturelimit"),
	G_MAX_HOLOCRON_CARRY("g_maxHolocronCarry"),
	G_PRIVATE_DUEL("g_privateDuel"),
	G_SABER_LOCKING("g_saberLocking"),
	G_MAX_FORCE_RANK("g_maxForceRank"),
	DUEL_FRAG_LIMIT("duel_fraglimit"),
	G_FORCE_BASED_TEAMS("g_forceBasedTeams"),
	G_DUEL_WEAPON_DISABLE("g_duelWeaponDisable"),
	G_NEED_PASS("g_needpass"),
	SV_ALLOW_DOWNLOAD("sv_allowDownload"),
	BOT_MIN_PLAYERS("bot_minplayers"),
	GAME_NAME("gamename"),
	G_GRAVITY("g_gravity"),
	TIME_ADD_TIME_ADD_SCQ ("scq"),
	BG_FIGHTER_ALT_CONTROL("bg_fighterAltControl"),
	G_SIEGE_TEAM1("g_siegeTeam1"),
	G_SIEGE_TEAM2("g_siegeTeam2"),
	RTV_RTM("RTVRTM"),
	
	UNKNOWN("Unknown");
	
	private final String key;
	
	ServerStatusType(String key) {
		this.key = key;
	}
	
	public static Optional<ServerStatusType> getEnumValueByKey(String key) {
		return Arrays.stream(ServerStatusType.values())
				.filter(serverInfoType -> serverInfoType.key.equals(key))
				.findFirst();
	}
}
