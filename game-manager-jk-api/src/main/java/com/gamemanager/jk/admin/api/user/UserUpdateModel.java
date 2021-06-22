package com.gamemanager.jk.admin.api.user;

import lombok.Data;

@Data
public class UserUpdateModel {
	
	private int permissions;
	private String password;
	
}
