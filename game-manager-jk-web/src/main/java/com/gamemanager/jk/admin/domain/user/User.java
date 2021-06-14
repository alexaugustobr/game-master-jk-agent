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
public class User implements UserDetails {
	
	private String username;
	
	private String password;
	
	private Role role;
	
	private boolean enabled = true;
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		String roleName = String.format("ROLE_%s", role.name());
		return Collections.singletonList(new SimpleGrantedAuthority(roleName));
	}
	
	@Override
	public String getUsername() {
		return username;
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
	
	public enum Role {
		RCON,
		SMOD,
		SYSTEM,
		;
	}
	
	@Override
	public String toString() {
		return username;
	}
}
