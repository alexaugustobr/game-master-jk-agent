package com.gamemanager.jk.admin.security;

import com.gamemanager.jk.admin.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MultiTenancyInterceptor extends OncePerRequestFilter {
	
	@Override
	public void doFilterInternal(HttpServletRequest request,
								 HttpServletResponse response,
								 FilterChain filterChain)
			throws IOException, ServletException {
		
		try {
			String[] split = request.getRequestURI().split("/");
			if (split.length <= 1) {
				filterChain.doFilter(request,response);
				return;
			}
			String uuidValue = split[1];
			
			UUID clientId = UUID.fromString(uuidValue);
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user.getType().equals(User.Type.CUSTOMER)
					&& !user.getCustomer().getId().equals(clientId)) {
				response.sendRedirect("/403");
			}
		} catch (Exception e) {
		
		}

		filterChain.doFilter(request, response);
		
	}
}
