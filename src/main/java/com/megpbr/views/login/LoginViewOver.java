package com.megpbr.views.login;



import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.megpbr.audit.Audit;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.views.dashboard.DashboardView;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;


@Route("logins")
@PageTitle("Login")
@AnonymousAllowed

public class LoginViewOver extends Div implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.LoginEvent> {
//public class LoginViewAudit extends Div implements BeforeEnterObserver {
	@Autowired
	Audit audit;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired SecurityContextRepository securityRepo;
	@Autowired UserDetailsServiceImpl userdetails;
	@Autowired SecurityService secservice;
	private static final String LOGIN_BACKGROUND_STYLE = "login";
	FormLayout form = new FormLayout();
    public TextField captchatext=new TextField();
    private Button loginbutton=new Button("Login");
    private static final String LOGIN_SUCCESS_URL = "/";
    TextField code = new TextField("");
    //LoginOverlay loginOverlay = new LoginOverlay();
    LoginForm loginOverlay = new LoginForm();
   // private final AuthenticatedUser authenticatedUser;
    public LoginViewOver() {
    	add(loginOverlay);
		loginOverlay.addLoginListener(this);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		
	}
	
	
	
	
	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(loginEvent.getUsername(), loginEvent.getPassword());
        Authentication authenticated = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        SecurityContext context = SecurityContextHolder.getContext();
        securityRepo.saveContext(context, VaadinServletRequest.getCurrent(), VaadinServletResponse.getCurrent());
		  if (authenticated.isAuthenticated()) {
			  UI.getCurrent().navigate(DashboardView.class); 
		  } else {
			  System.out.println("FAILURE");
		  Notification.show("Authentication failed", 3000,
		  Notification.Position.BOTTOM_CENTER); }
		 
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		

		loginOverlay.setError(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
  
}