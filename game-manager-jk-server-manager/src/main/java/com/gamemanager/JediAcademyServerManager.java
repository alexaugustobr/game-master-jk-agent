package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
public class JediAcademyServerManager {

	private final JediAcademyServerConnector connector;
	
	public AnonymousCommandSender asAnonymous() {
		return new AnonymousCommandSender(connector);
	}
	
	public SmodCommandSender asSmod(String pass) {
		return new SmodCommandSender(connector, pass);
	}
	
	public RootCommandSender asRoot(String pass) {
		return new RootCommandSender(connector, pass);
	}
	
	@AllArgsConstructor
	@Getter
	public static class AnonymousCommandSender {
		
		private final JediAcademyServerConnector connector;
		
		private static final String INFO_COMMAND = "getinfo";
		private static final String STATUS_COMMAND = "getstatus";
		
		public Map<ServerStatusType, String> getBasicInfo() throws CommunicatingWithServerException {
			try {
				byte[] outputMessage = connector.executeCommand(INFO_COMMAND);
				return ServerStatusTranslator.translate(outputMessage);
			} catch (Exception e) {
				throw new CommunicatingWithServerException(ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER, e);
			}
		}
		
		public Map<ServerStatusType, String> getDetailedStatus() throws CommunicatingWithServerException {
			try {
				byte[] outputMessage = connector.executeCommand(STATUS_COMMAND);
				return ServerStatusTranslator.translate(outputMessage);
			} catch (Exception e) {
				throw new CommunicatingWithServerException(ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER, e);
			}
		}
		
		public int getPlayerCount() throws CommunicatingWithServerException {
			Map<ServerStatusType, String> status = this.getBasicInfo();
			return Integer.parseInt(status.getOrDefault(ServerStatusType.CLIENTS, "0"));
		}
		
	}
	
	@Getter
	public static class SmodCommandSender extends AnonymousCommandSender {
		
		private final String pass;
		
		public SmodCommandSender(JediAcademyServerConnector connector, String pass) {
			super(connector);
			this.pass = pass;
		}
		
		public void changeMap(String mapName) {
			//connector()
		}
		
	}
	
	@Getter
	public static class RootCommandSender extends SmodCommandSender {
		
		public RootCommandSender(JediAcademyServerConnector connector, String pass) {
			super(connector, pass);
		}
		
		public void resetServer() {
			//connector()
		}
	
	}
}
