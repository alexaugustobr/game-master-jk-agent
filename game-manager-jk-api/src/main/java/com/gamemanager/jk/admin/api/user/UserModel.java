package com.gamemanager.jk.admin.api.user;

import lombok.Data;

@Data
public class UserModel {
	
	private int slot;
	private String username;
	private String type;
	private int permissions;
	private boolean enabled;
	private String password;
	
}
