package com.gamemanager.jk.admin.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileInput {
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String name;
	@NotBlank
	@Email
	@Size(min = 3, max = 100)
	private String email;
	
}
