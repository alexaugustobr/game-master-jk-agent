package com.gamemanager;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ServerStatusTranslatorTest {

	@Test
	public void dado_uma_string_de_status_deve_traduzir_para_mapa() throws UnreadableMessageException {
		
		String successServerMessage = "����statusResponse\n\\g_DuelFriendlyTeam\\0\\g_DuelTimeLimit\\900\\g_jediVmerc\\0\\g_HideHUDFromSpecs\\1\\g_noSpecMove\\0\\g_TKPointsKickCount\\9999\\g_TKPointsSpecCount\\9998\\g_AntiCheat\\0\\g_Authenticity\\0\\g_ShuffleTimer\\300\\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate\\25000\\sv_maxclients\\32\\sv_hostname\\Test Server\\g_gametype\\7\\timelimit\\0\\fraglimit\\15\\version\\JAmp: v1.0.1.1 linux-i386 Nov 10 2003\\dmflags\\0\\capturelimit\\0\\g_maxHolocronCarry\\3\\g_privateDuel\\1\\g_saberLocking\\1\\g_maxForceRank\\6\\duel_fraglimit\\10\\g_forceBasedTeams\\0\\g_duelWeaponDisable\\1\\g_needpass\\0\\protocol\\26\\mapname\\mb2_smuggler\\sv_allowDownload\\0\\bot_minplayers\\0\\gamename\\Movie Battles II V1.6.2.1\\g_gravity\\800\\TimeAdd\\0\\scq\\aaaaaaaaaaaa\\bg_fighterAltControl\\0\\g_siegeTeam1\\LEG_Good\\g_siegeTeam2\\LEG_Evil\\RTVRTM\\1928/3.6c\n";
		
		Map<ServerStatusType, String> translate = ServerStatusTranslator.translate(successServerMessage.getBytes());
		
		Assert.assertEquals("15", translate.get(ServerStatusType.FRAG_LIMIT));
		Assert.assertEquals(42, translate.keySet().size());
		
	}
	
	@Test
	public void dado_uma_string_de_info_deve_traduzir_para_mapa() throws UnreadableMessageException {
		
		String successInfoMessage = "����infoResponse\n\\game\\MBII\\fdisable\\0\\wdisable\\40\\truejedi\\0\\needpass\\0\\gametype\\7\\sv_maxclients\\32\\clients\\0\\mapname\\mb2_smuggler\\hostname\\Test Server\\protocol\\26";
		
		Map<ServerStatusType, String> translate = ServerStatusTranslator.translate(successInfoMessage.getBytes());
		
		Assert.assertEquals("MBII", translate.get(ServerStatusType.GAME));
		Assert.assertEquals(11, translate.keySet().size());
		
	}
	
	@Test(expected = UnreadableMessageException.class)
	public void dado_uma_string_mensagem_mal_formatada_deve_retornar_exception() throws UnreadableMessageException {
		
		String successServerMessage = "NODE_NODE_NODE\\NODE_NODE_NODE";
		
		ServerStatusTranslator.translate(successServerMessage.getBytes());
		
	}
	
	@Test(expected = UnreadableMessageException.class)
	public void dado_uma_array_nulo_deve_retornar_exception() throws UnreadableMessageException {
		ServerStatusTranslator.translate(null);
	}
	
	@Test(expected = UnreadableMessageException.class)
	public void dado_uma_array_vazio_deve_retornar_exception() throws UnreadableMessageException {
		ServerStatusTranslator.translate(new byte[0]);
	}

}