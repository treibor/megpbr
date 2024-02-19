package com.megpbr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.megpbr.audit.Audit;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("login")
@PageTitle("Login")
@AnonymousAllowed

public class LoginViewAudit extends Div implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.LoginEvent> {
//public class LoginViewAudit extends Div implements BeforeEnterObserver {
	@Autowired
	Audit audit;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	UserDetailsServiceImpl userdetails;
	@Autowired
	SecurityService securityservice;
	FormLayout form = new FormLayout();
    public TextField captchatext=new TextField();
    private Button loginbutton=new Button("Login");
    private static final String LOGIN_SUCCESS_URL = "/megpbr";
    TextField code = new TextField("");
    LoginOverlay loginOverlay = new LoginOverlay();
	public LoginViewAudit() {
		add(loginOverlay);
		loginOverlay.setTitle("Meghalaya Biodiversity Board");
		loginOverlay.setDescription("People's Biodiversity Register");
		loginOverlay.setForgotPasswordButtonVisible(false);
		loginOverlay.setOpened(true);
		//loginOverlay.addLoginListener(this);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		/*
		 * if(SecurityUtils.isAuthenticated()) {
		 * UI.getCurrent().getPage().setLocation(LOGIN_SUCCESS_URL); }
		 */
		loginOverlay.setAction("login");
		//VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	   
	
	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
	    try {
	    	final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginEvent.getUsername(), loginEvent.getPassword())); // 
                // if authentication was successful we will update the security context and redirect to the page requested first
                SecurityContextHolder.getContext().setAuthentication(authentication); // 
                loginOverlay.close();
	        UI.getCurrent().navigate(LOGIN_SUCCESS_URL);
	    } catch (AuthenticationException e) {
	        audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
	        loginOverlay.setError(true);
	    }
	}

	@Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            loginOverlay.setError(true);
        }
    }
  
}