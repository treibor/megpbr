package com.megpbr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.servlet.ServletException;

@Component
public class SecurityService {
	private final AuthenticationContext authenticationContext;
	@Autowired UserDetailsServiceImpl userdetails;
	@Autowired BCryptPasswordEncoder passwordEncoder;	
	
	public SecurityService(AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}
	
	public UserDetails getAuthenticatedUser1() {
		System.out.println("USER"+authenticationContext.getAuthenticatedUser(UserDetails.class).get());
		return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
	}
	public UserDetails getAuthenticatedUser2() {
		//System.out.println("USER"+authenticationContext.getAuthenticatedUser(UserDetails.class).get());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (UserDetails) principal;
		//SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public boolean canLogin(String user, String password) {
		return true;
	}
	public void logout() {
		//authenticationContext.
		authenticationContext.logout();
	}

	public UserDetails getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				return (UserDetails) principal;
			}
		}
		return null;
	}

	public UserDetails getUser() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Object principal = securityContext.getAuthentication().getPrincipal();
		if (principal == null) {
				return null;
		}

		return (UserDetails) principal;
	}
	

}
