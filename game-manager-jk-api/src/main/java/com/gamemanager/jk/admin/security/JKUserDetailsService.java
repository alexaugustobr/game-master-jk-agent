package com.gamemanager.jk.admin.security;

import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JKUserDetailsService implements UserDetailsService {
	
	private final UserRepository users;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		return users.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
	}
}
