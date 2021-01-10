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
	
	@Test
	public void test() {
		String msg = "����statusResponse\n" +
				"\\fraglimit\\25\\g_AntiCheat\\1\\g_Authenticity\\3\\g_competitive\\0\\g_DuelFriendlyTeam\\0\\g_DuelTimeLimit\\900\\g_gametype\\7\\g_jediVmerc\\1\\g_ShuffleTimer\\300\\sv_autoDemo\\0\\sv_floodProtect\\1\\sv_fps\\40\\sv_hostname\\^0[LS] ^1Knights Den ^7II\\sv_maxclients\\32\\sv_maxPing\\0\\sv_maxRate\\25000\\sv_minPing\\0\\sv_privateClients\\0\\timelimit\\0\\TK_Kick\\750\\TK_Spec\\500\\version\\JAmp: v1.0.1.0 linux-i386 Nov 12 2020\\dmflags\\0\\capturelimit\\0\\g_maxHolocronCarry\\3\\g_privateDuel\\1\\g_saberLocking\\1\\g_maxForceRank\\7\\duel_fraglimit\\10\\g_forceBasedTeams\\0\\g_duelWeaponDisable\\1\\g_needpass\\0\\protocol\\26\\mapname\\mb2_dotf\\sv_allowDownload\\0\\bot_minplayers\\0\\discord\\https://discord.gg/CujXD7UJJY\\gamename\\Movie Battles II V1.7.1\\g_gravity\\800\\TimeAdd\\0\\scq\\aaaaaaaaaaaa\\bg_fighterAltControl\\0\\g_siegeTeam1\\LEG_Good\\g_siegeTeam2\\LEG_Evil\\RTVRTM\\1928/3.6c\n" +
				"0 135 \"samuel daddy^6\"\n" +
				"9 25 \"nerevar\"\n" +
				"0 29 \"^1Revann\"\n" +
				"0 32 \"I Get It Im ^0BLACK\"\n" +
				"0 25 \"^6Sneaki^2Actual\"\n" +
				"5 26 \"blue scrub\"\n" +
				"5 25 \"a friend\"\n" +
				"0 47 \"Titus Augustine\"\n" +
				"0 55 \"^0[Longsword] ^1Rojo^3Dragon\"\n" +
				"0 45 \"weirdaVID\"\n" +
				"1 122 \"Rum\"\n";
		
		
		String[] split = msg.split("\n");
		
		System.out.println(split);
		
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