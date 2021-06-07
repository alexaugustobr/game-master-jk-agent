package com.gamemanager.jk.admin.domain.server;

public class NameUtils {
	public static String removeColorTags(String name) {
		return name.replaceAll("\\^+[0-9]", "");
	}
}
