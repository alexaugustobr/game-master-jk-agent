package com.gamemanager.jk.admin.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordInput {
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String password;
	
}
