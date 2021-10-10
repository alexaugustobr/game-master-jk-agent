package com.gamemanager.jk.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {
	
	@Value("${allowedOrigins:*}")
	private String[] allowedOrigins;
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		CorsConfiguration config = new CorsConfiguration();
		if (allowedOrigins[0].equals("*")) {
			config.setAllowCredentials(false);
		} else {
			config.setAllowCredentials(true);
		}
		
		config.setAllowedOrigins(Arrays.asList(allowedOrigins));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return bean;
	}

}