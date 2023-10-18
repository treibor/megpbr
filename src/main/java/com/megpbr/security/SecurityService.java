package com.megpbr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.views.villages.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Component
public class SecurityService {
	private static final String LOGOUT_SUCCESS_URL = "/";
	@Autowired
	AuditRepository arepo;
	private final AuthenticationContext authenticationContext;

    
	public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }
	
	public UserDetails getAuthenticatedUser() {
		//System.out.println("Authenticated User");
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return (UserDetails) context.getAuthentication().getPrincipal();
		}
		// Anonymous or no authentication.
		return null;

	}

	public void logout() {
		//System.out.println("Logout");
		//UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
		//SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		//logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
		authenticationContext.logout();
	}
	
	
}
