package com.megpbr.views.login;

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
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;


//@Route("loginss")
@PageTitle("Login")
@AnonymousAllowed

public class CustomView extends Div implements BeforeEnterObserver{
//public class LoginViewAudit extends Div implements BeforeEnterObserver {
	@Autowired
	Audit audit;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	SecurityService securityservice;
	@Autowired UserDetailsServiceImpl userdetails;
	private static final String LOGIN_BACKGROUND_STYLE = "login";
	FormLayout form = new FormLayout();
    public TextField captchatext=new TextField();
    private Button loginbutton=new Button("Login");
    private static final String LOGIN_SUCCESS_URL = "/";
    TextField code = new TextField("");
    TextField username=new TextField();
	PasswordField password=new PasswordField();
    public CustomView() {
    	add(getForm());
    	
	}
	public Component getForm() {
		var form=new FormLayout();
		
		Button loginbutton=new Button("login");
		loginbutton.addClickListener(e-> doLogin());
		form.add(username, password, loginbutton);
		return form;
	}
    
	public void doLogin() {
		try {
			//securityservice.authenticateUser2(username.getValue(), password.getValue());
			//VaadinSession session = VaadinSession.getCurrent();
			getUI().ifPresent(ui -> ui.navigate("/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
           // loginOverlay.setError(true);
        }
    }
  
}