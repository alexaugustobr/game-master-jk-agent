package com.gamemanager.jk.admin.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {
	
	private String userName;
	
	private String password;
	
	private Type type;
	
	private boolean enabled = true;
	
	private Integer slot;
	
	private Integer permissions;
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		String roleName = String.format("ROLE_%s", type.name());
		return Collections.singletonList(new SimpleGrantedAuthority(roleName));
	}
	
	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public enum Type {
		RCON,
		SMOD,
		SYSTEM,
		;
	}
	
	@Override
	public String toString() {
		return userName;
	}
}
