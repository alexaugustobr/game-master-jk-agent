package com.gamemanager.jk.admin.api.user;

import lombok.Data;

@Data
public class UserModel {
	
	private int slot;
	private String name;
	private String type;
	private int permissions;
	private String password;
	private boolean enabled;
	
}
