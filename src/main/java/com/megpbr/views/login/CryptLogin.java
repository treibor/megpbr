package com.megpbr.views.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;

import com.megpbr.audit.Audit;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.captcha.Captcha;
import com.megpbr.security.captcha.CapthaImpl;
import com.megpbr.views.dashboard.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import elemental.json.JsonObject;

@Route("login")
@AnonymousAllowed
public class CryptLogin extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	Audit audit;
	@Autowired
	SessionRegistry sr;
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
	H2 title = new H2("Meghalaya Biodiversity Board");
	H3 description = new H3("People's Biodiversity Register");
	Anchor anchor = new Anchor();
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	public CryptLogin(AuthenticatedUser authenticatedUser) {
		this.authenticatedUser = authenticatedUser;

		button.addClickListener(e -> {
			String username = usernameField.getValue();
			String password = passwordField.getValue();
			UI.getCurrent().getPage().executeJs("var key = 'iadei';" + "var encryptedUsername = '';"
					+ "for (var i = 0; i < $0.length; i++) {"
					+ "    encryptedUsername += String.fromCharCode($0.charCodeAt(i) ^ key.charCodeAt(i % key.length));"
					+ "}" + "var encryptedPassword = '';" + "for (var j = 0; j < $1.length; j++) {"
					+ "    encryptedPassword += String.fromCharCode($1.charCodeAt(j) ^ key.charCodeAt(j % key.length));"
					+ "}" + "return { username: btoa(encryptedUsername), password: btoa(encryptedPassword) };",
					username, password).then(response -> {
						JsonObject data = (JsonObject) response;
						String encryptedUsername = data.getString("username");
						String encryptedPassword = data.getString("password");
						doLogin(encryptedUsername, encryptedPassword);
					});
		});
		// setAlignItems(FlexComponent.Alignment.STRETCH);
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setSizeFull();
		add(createPasswordForm());
		getStyle().set("background-color", "hsla(0, 0%, 95%, 0.69)");
		//add(createLoginForm());
	}
	
	private Component createPasswordForm() {

		// captchacontainer.add(getCaptcha(), refreshButton);
		usernameField.setRequired(true);
		usernameField.setMinLength(5);
		usernameField.setMaxLength(40);
		passwordField.setRequired(true);
		passwordField.setMinLength(5);
		passwordField.setMaxLength(40);
		captchatext.setPlaceholder("ENTER CAPTCHA");
		captchatext.setMaxLength(6);
		captchatext.setMinLength(6);
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.addClickShortcut(Key.ENTER);
		anchor.setText("Forgot Password?");
		anchor.getElement().addEventListener("click",
				e -> Notification.show("To Be Implemented Via Email API in Production: Public IP required."));
		anchor.getStyle().set("color", "hsla(119, 93%, 29%, 0.90)").set("padding-bottom", "20px");
		var form = new FormLayout();
		// form.add(title, 1);
		// form.add(description, 1);
		form.add(usernameField, 1);
		form.add(passwordField, 1);
		
		form.add(getCaptcha(), 1);
		form.add(new Span(), 1);
		form.add(captchatext, 1);
		//form.add(, 1);
		form.add(button, 1);
		//form.add(anchor, 1);
		form.setResponsiveSteps
			(new ResponsiveStep("0", 1), 
					new ResponsiveStep("300px", 1)
					);
		form.setWidth("320px");
		form.getStyle().set("padding", "20px");
		
		var header = new VerticalLayout();
		title.getStyle().set("color", "white");
	    description.getStyle().set("color", "white");
		header.add(title, description);
		header.getStyle().set("background-color", "hsla(119, 93%, 29%, 0.90)");
		header.setAlignItems(Alignment.START);
		header.setJustifyContentMode(JustifyContentMode.END);
		header.setHeight("150px");
		header.getStyle().set("border-radius", "10px 10px 0 0");
		var container = new VerticalLayout();
		container.setSizeUndefined();
		container.getStyle().set("background-color", "white");
		//container.getStyle().set("border", "2px solid black");
		container.getStyle().set("border-radius", "10px");
		container.getStyle().set("padding", "0px");
		container.setAlignItems(Alignment.CENTER);
		container.setJustifyContentMode(JustifyContentMode.CENTER);
		container.add(header, form, anchor);
		/*
		 * var wrapper = new VerticalLayout(); wrapper.setSizeFull();
		 * wrapper.setAlignItems(Alignment.CENTER);
		 * wrapper.setJustifyContentMode(JustifyContentMode.CENTER);
		 * wrapper.add(title,description, container);
		 */
		return container;
	}

	private void doLogin(String encryptedUsername, String encryptedPassword) {
		String username;
		String password;
		try {
			
			username = CryptUtils.decryptUsername(encryptedUsername);
			password = CryptUtils.decryptPassword(encryptedPassword);
			invalidatePreviousSessionsForUser(username);
			//System.out.println("Active Sessions: " + getActiveSessionCountForUser(username));
			//
		} catch (Exception e) {
			//Notification.show("Error decrypting credentials");
			//e.printStackTrace();
			clearFields();
			return;
		}

		if (captcha.checkUserAnswer(captchatext.getValue())) {
			

			
			try {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
				Authentication authentication = this.authenticationManager.authenticate(token);

				SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
				context.setAuthentication(authentication);
				this.securityContextHolderStrategy.setContext(context);
				securityRepo.saveContext(context, VaadinServletRequest.getCurrent(),
						VaadinServletResponse.getCurrent());
				// registerSession(ServletContext, (UserDetails) authentication.getPrincipal());
				registerSession(VaadinService.getCurrentRequest().getWrappedSession(), (UserDetails) authentication.getPrincipal());
				audit.saveAudit("Login Successful", username);
				UI.getCurrent().navigate(HomeView.class);

			} catch (Exception e) {
				
				//e.printStackTrace();
				Notification.show("Authentication failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
				clearFields();
				
				audit.saveAudit("Login Failure- Credentials", username);
			}
		} else {
			Notification.show("Invalid captcha").addThemeVariants(NotificationVariant.LUMO_ERROR);
			clearFields();
			audit.saveAudit("Captcha Failure", username);
			
			
		}
	}
	private void registerSession(WrappedSession session, UserDetails userDetails) {
        sr.registerNewSession(session.getId(), userDetails);
    }
	

	public int getActiveSessionCountForUser(String username) {
		int count = 0;
		List<Object> principals = sr.getAllPrincipals();
		for (Object principal : principals) {
			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;
				if (userDetails.getUsername().equals(username)) {
					List<SessionInformation> sessionInfoList = sr.getAllSessions(userDetails, true); // Invalidate
																										// previous
																										// sessions
					count += sessionInfoList.size();
				}
			}
		}
		return count;
	}

	public void invalidatePreviousSessionsForUser(String username) {
		// Get active session count for the user
		int activeSessionCount = getActiveSessionCountForUser(username);

		// If there are active sessions for the user, invalidate them
		if (activeSessionCount > 0) {
			List<Object> principals = sr.getAllPrincipals();
			for (Object principal : principals) {
				if (principal instanceof UserDetails) {
					UserDetails userDetails = (UserDetails) principal;
					if (userDetails.getUsername().equals(username)) {
						List<SessionInformation> sessionInfoList = sr.getAllSessions(userDetails, true); // Invalidate
																											// previous
																											// sessions
						for (SessionInformation sessionInfo : sessionInfoList) {
							sessionInfo.expireNow(); // Expire session information

						}
					}
				}
			}
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
		captchacontainer.getStyle().set("padding", "20px");
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
			// loginOverlay.setOpened(false);
			event.forwardTo("");
		}

		// loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
}