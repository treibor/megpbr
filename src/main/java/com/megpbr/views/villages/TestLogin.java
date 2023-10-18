package com.megpbr.views.villages;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.megpbr.audit.CaptchaCheck;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.UserRepository;
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
@Route("login2")
@PageTitle("Login")
@AnonymousAllowed

@Tag("form")
public class TestLogin extends HtmlContainer implements BeforeEnterObserver{

    public TestLogin() {
        //addClassNames(LumoUtility.Display.FLEX,
    	Input userNameField = new Input();
		userNameField.setWidth("100%");
		userNameField.setId("username");
		userNameField.getElement().setAttribute("autocomplete", "username");
		userNameField.getClassNames().add("login-textfield");

Input passwordField = new Input();
		passwordField.setWidth("100%");
		passwordField.setId("password");
		passwordField.getElement().setAttribute("autocomplete", "current-password");
		passwordField.getElement().setAttribute("type", "password");
		passwordField.getClassNames().add("login-textfield");

		final VerticalLayout content = new VerticalLayout();
		content.add(
				new com.vaadin.flow.component.html.Label("User name"), userNameField,
				new com.vaadin.flow.component.html.Label("Password"),
				passwordField);
        add(content);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            //login.setError(true);
        }
    }
}