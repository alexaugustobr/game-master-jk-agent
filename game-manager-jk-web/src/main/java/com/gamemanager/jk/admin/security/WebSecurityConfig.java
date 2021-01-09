package com.gamemanager.jk.admin.security;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/img/**").permitAll()
				.antMatchers("/fonts/**").permitAll()
			.antMatchers("/admin/**")
				.hasAnyRole("ROOT_BUSINESS", "ADMIN_BUSINESS", "ASSISTANT_BUSINESS")
			.antMatchers("/[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}/**")
				.hasAnyRole("ROOT_BUSINESS", "ADMIN_BUSINESS", "ASSISTANT_BUSINESS", "ROOT_CUSTOMER", "ADMIN_CUSTOMER", "ASSISTANT_CUSTOMER")
			.antMatchers("/login*").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error=true")
			.and()
				.logout()
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID");
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
}
