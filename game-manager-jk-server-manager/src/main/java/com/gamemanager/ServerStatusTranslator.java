package com.gamemanager;

import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerStatusTranslator {
	
	private ServerStatusTranslator() {}
	
	private static final String[] VALID_MESSAGE_HEADERS = {"infoResponse","statusResponse"};
	
	private static final String REPLACEMENT_PLACE_HOLDER = "#SPLIT#";
	
	public static List<String> playerList(byte[] byteMessage) throws UnreadableMessageException {
		if (byteMessage == null || byteMessage.length == 0) {
			throw new UnreadableMessageException(ErrorMessages.ERROR_MESSAGE_IS_EMPTY);
		}
		
		String statusMessage = new String(byteMessage);
		
		String[] rawPlayerList = statusMessage.split("\n");
		
		rawPlayerList[0] = null; //status
		rawPlayerList[1] = null; //status
		
		rawPlayerList[rawPlayerList.length-1] = null;  //unused
		
		List<String> playerNames = new ArrayList<>();
		
		for (String rawPlayerName : rawPlayerList) {
			if (StringUtils.isNotBlank(rawPlayerName)) {
				
				try {
					final String regex = "(\"(.*?)\")";
					Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
					Matcher matcher = pattern.matcher(rawPlayerName);
					
					matcher.find();
					
					playerNames.add(matcher.group(2));
				} catch (Exception e) {
				
				}
			}
		}
		
		return playerNames;
	}
	
	public static Map<ServerStatusType, String> translate(byte[] byteMessage) throws UnreadableMessageException {
		
		if (byteMessage == null || byteMessage.length == 0) {
			throw new UnreadableMessageException(ErrorMessages.ERROR_MESSAGE_IS_EMPTY);
		}
		
		String statusMessage = new String(byteMessage);
		
		if (!containsAnyValidHeader(statusMessage)) {
			throw new UnreadableMessageException(ErrorMessages.ERROR_MESSAGE_HAS_NO_VALID_HEADER);
		}
		
		String messageBody = extractMessageBody(statusMessage);
		
		List<String> keyValuesList = Arrays.asList(messageBody.split(REPLACEMENT_PLACE_HOLDER));
		
		return transformIntoMap(keyValuesList);
	}
	
	private static Map<ServerStatusType, String> transformIntoMap(List<String> keyValuesList) {
		Map<ServerStatusType, String> map = new EnumMap<>(ServerStatusType.class);
		
		Iterator<String> iterator = keyValuesList.iterator();
		
		while (iterator.hasNext()) {
			
			String key = iterator.next();
			
			String value = null;
			if (iterator.hasNext()) {
				value = iterator.next();
			}
			
			if (key != null && value != null) {
				map.put(ServerStatusType.getEnumValueByKey(key).orElse(ServerStatusType.UNKNOWN), value);
			}
		}
		
		return map;
	}
	
	/*
	 * Removes the message header
	 */
	private static String extractMessageBody(String statusMessage) {
		//+2 to ignore the first \ char in order to split correctly
		int begin = statusMessage.indexOf("\n") + 2;
		
		return statusMessage.substring(begin)
					.replace("\\", REPLACEMENT_PLACE_HOLDER);
	}
	
	private static boolean containsAnyValidHeader(String statusMessage) {
		return Arrays.stream(VALID_MESSAGE_HEADERS).anyMatch(statusMessage::contains);
	}
	
}
