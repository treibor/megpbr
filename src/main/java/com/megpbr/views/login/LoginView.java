package com.megpbr.views.login;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.megpbr.security.CaptchaCheck;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.views.MainLayout;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("loginx")
@PageTitle("Login")
@AnonymousAllowed

public class LoginView extends Div implements BeforeEnterObserver{
	FormLayout form = new FormLayout();
	public TextField captchatext = new TextField();
	public Button captchabutton=new Button("");
	private static final String LOGIN_SUCCESS_URL = "/";
	public TextField code = new TextField("");
	LoginOverlay loginOverlay = new LoginOverlay();
	public LoginView() {
	    add(loginOverlay);
		loginOverlay.setTitle("Meghalaya Biodiversity Board");
		loginOverlay.setDescription("People's Biodiversity Register Ver. 2.0");
		loginOverlay.setForgotPasswordButtonVisible(false);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		//code.getElement().setAttribute("name", "code");
		//loginOverlay.getCustomFormArea().add(code);
		loginOverlay.setOpened(true);
		loginOverlay.setAction("login");
		//loginOverlay.addLoginListener(loginEventComponentEventListener());
	}
	
	

	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if (beforeEnterEvent.getLocation()
				.getQueryParameters().getParameters().containsKey("error")) {
			loginOverlay.setError(true);
		}

	}
  
}
