package com.gamemanager.jk.admin.api.user;

import lombok.Data;

@Data
public class UserUpdateModel {
	
	private String userName;
	private int permissions;
	private String password;
	private boolean enabled;
	
}
