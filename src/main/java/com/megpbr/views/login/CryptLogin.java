package com.megpbr.views.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.captcha.Captcha;
import com.megpbr.security.captcha.CapthaImpl;
import com.megpbr.views.dashboard.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import elemental.json.JsonObject;

@Route("slogin")
@AnonymousAllowed
public class CryptLogin extends Div implements BeforeEnterObserver{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    SecurityContextRepository securityRepo;
    private final AuthenticatedUser authenticatedUser;
    HorizontalLayout captchacontainer = new HorizontalLayout();
    Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    Captcha captcha = new CapthaImpl();
    Image image;
    public TextField captchatext = new TextField();
    TextField usernameField = new TextField("User Name");
    PasswordField passwordField = new PasswordField("Password");
    Button button = new Button("Submit");

    public CryptLogin(AuthenticatedUser authenticatedUser) {
    	this.authenticatedUser=authenticatedUser;
    	add(createPasswordForm());
        button.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            UI.getCurrent().getPage().executeJs(
                "var key = 'iadei';" +
                "var encryptedUsername = '';" +
                "for (var i = 0; i < $0.length; i++) {" +
                "    encryptedUsername += String.fromCharCode($0.charCodeAt(i) ^ key.charCodeAt(i % key.length));" +
                "}" +
                "var encryptedPassword = '';" +
                "for (var j = 0; j < $1.length; j++) {" +
                "    encryptedPassword += String.fromCharCode($1.charCodeAt(j) ^ key.charCodeAt(j % key.length));" +
                "}" +
                "return { username: btoa(encryptedUsername), password: btoa(encryptedPassword) };",
                username, password).then(response -> {
                    JsonObject data = (JsonObject) response;
                    String encryptedUsername = data.getString("username");
                    String encryptedPassword = data.getString("password");
                    doLogin(encryptedUsername, encryptedPassword);
                });
        });

       
    }

    private Component createPasswordForm() {
        var title = new H2("Meghalaya Biodiversity Board");
        var description = new H3("People's Biodiversity Register Ver. 2.0");
        //captchacontainer.add(getCaptcha(), refreshButton);
        usernameField.setRequired(true);
        usernameField.setMinLength(5);
        usernameField.setMaxLength(20);
        passwordField.setRequired(true);
        passwordField.setMinLength(5);
        passwordField.setMaxLength(30);
        captchatext.setPlaceholder("ENTER CAPTCHA");
        captchatext.setMaxLength(6);
        captchatext.setMinLength(6);

        var form = new FormLayout();
        form.add(title, 1);
        form.add(description, 1);
        form.add(usernameField, 1);
        form.add(passwordField, 1);
        form.add(getCaptcha(), 1);
        form.add(captchatext, 1);
        form.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("300px", 1)
        );
        form.setWidth("300px");

        var container = new VerticalLayout();
        container.setSizeFull();
        container.add(form, button);
        container.setAlignItems(Alignment.CENTER);
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.getStyle().setColor("red");

        var wrapper = new HorizontalLayout();
        wrapper.setSizeFull();
        wrapper.setAlignItems(Alignment.CENTER);
        wrapper.setJustifyContentMode(JustifyContentMode.CENTER);
        wrapper.add(container);

        return wrapper;
    }

    private void doLogin(String encryptedUsername, String encryptedPassword) {
        if (captcha.checkUserAnswer(captchatext.getValue())) {
            String username = CryptUtils.decryptUsername(encryptedUsername);
            String password = CryptUtils.decryptPassword(encryptedPassword);
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
                Authentication authenticated = authenticationManager.authenticate(authentication);
                SecurityContextHolder.getContext().setAuthentication(authenticated);

                if (authenticated.isAuthenticated()) {
                    SecurityContext context = SecurityContextHolder.getContext();
                    securityRepo.saveContext(context, VaadinServletRequest.getCurrent(), VaadinServletResponse.getCurrent());
                    UI.getCurrent().navigate(HomeView.class);
                } else {
                    Notification.show("Error");
                    clearFields();
                }
            } catch (Exception e) {
                Notification.show("Authentication failed");
                clearFields();
            }
        } else {
            Notification.show("Invalid captcha");
            clearFields();
        }
    }

    private void clearFields() {
        passwordField.setValue("");
        usernameField.setValue("");
        captchatext.setValue("");
    }

    public Component getCaptcha() {
        image = captcha.getCaptchaImg();
        captchacontainer.add(image, refreshButton);
        refreshButton.addClickListener(e -> regenerateCaptcha());
        captchacontainer.setWidthFull();
        captchacontainer.setJustifyContentMode(JustifyContentMode.CENTER);
        return captchacontainer;
    }

    private void regenerateCaptcha() {
        captchacontainer.remove(image);
        captchacontainer.remove(refreshButton);
        image = captcha.getCaptchaImg();
        captchacontainer.add(image, refreshButton);
    }

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (authenticatedUser.get().isPresent()) {
			// Already logged in
			//loginOverlay.setOpened(false);
			event.forwardTo("");
		}

		//loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
}