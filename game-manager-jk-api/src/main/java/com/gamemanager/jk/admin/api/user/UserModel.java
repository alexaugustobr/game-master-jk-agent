package com.gamemanager.jk.admin.api.user;

import lombok.Data;

@Data
public class UserModel {
	
	private int slot;
	private String userName;
	private String type;
	private int permissions;
	private boolean enabled;
	
}
