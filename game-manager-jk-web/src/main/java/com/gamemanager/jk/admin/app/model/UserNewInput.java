package com.gamemanager.jk.admin.app.model;

import com.gamemanager.jk.admin.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNewInput {
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String name;
	@NotBlank
	@Email
	@Size(min = 3, max = 100)
	private String email;
	
	@NotNull
	private User.Role role;
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String initialPassword;
	
}
