package com.megpbr.views.login;


import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
@Route("ssslogin")
@Uses(LoginForm.class)
public class NewLogin extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm loginForm = new LoginForm();

    public NewLogin() {
        // Customize login form and i18n messages if needed
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Login to CryptApp");
        i18n.getForm().setUsername("Username");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Login");
        i18n.getForm().setForgotPassword("Forgot password?");
        loginForm.setI18n(i18n);
        
        // Add event listeners for login and forgot password events
       // loginForm.addLoginListener(this::handleLogin);
        loginForm.addForgotPasswordListener(event -> {
            Notification.show("Redirect to forgot password page");
        });
        
        // Add the login form to the layout
        add(loginForm);
    }

  
    

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if the user is already authenticated
        if (isUserLoggedIn()) {
            // Redirect to the main view if the user is already logged in
            event.forwardTo("main");
        }
    }

    private boolean isUserLoggedIn() {
        // Implement your logic to check if the user is logged in
        // For demonstration, return false to always show the login page
        return false;
    }
}