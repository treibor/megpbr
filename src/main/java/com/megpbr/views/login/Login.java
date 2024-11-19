package com.megpbr.views.login;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.service.EmailSender;
import com.megpbr.data.service.UserService;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.captcha.Captcha;
import com.megpbr.security.captcha.CapthaImpl;
import com.megpbr.views.dashboard.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class Login extends VerticalLayout implements BeforeEnterObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	Audit audit;
	@Autowired
	SessionRegistry sr;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	SecurityContextRepository securityRepo;
	@Autowired
	UserService uservice;
	private final AuthenticatedUser authenticatedUser;
	HorizontalLayout captchacontainer = new HorizontalLayout();
	HorizontalLayout captchacontainer2 = new HorizontalLayout();
	Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
	Button refreshButton2 = new Button(new Icon(VaadinIcon.REFRESH));
	Captcha captcha = new CapthaImpl();
	Captcha captcha2 = new CapthaImpl();
	Image image;
	Image image2;
	public TextField captchatext = new TextField();
	public TextField captchatext2 = new TextField();
	TextField usernameField = new TextField("User Name");
	EmailField email = new EmailField();
	PasswordField passwordField = new PasswordField("Password");
	Button button = new Button("Login");
	H2 title = new H2("Meghalaya Biodiversity Board");
	H3 description = new H3("People's Biodiversity Register");
	Anchor anchor = new Anchor();
	final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();
	String dynamicKey = "";
	Dialog aboutdialog = new Dialog();

	public Login(AuthenticatedUser authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
		this.dynamicKey = generateDynamicKey();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setSizeFull();
		add(createPasswordForm());
		getStyle().set("background-color", "hsla(0, 0%, 95%, 0.69)");
	}

	public void createLoginFormxx() {
		TextField usernameField = new TextField("Username");
		PasswordField passwordField = new PasswordField("Password");
		Button loginButton = new Button("Login");
		TextField hiddenField = new TextField("login", "true");
		hiddenField.setVisible(false);
		usernameField.getElement().setAttribute("autocomplete", "off");
		passwordField.getElement().setAttribute("autocomplete", "off");

		loginButton.addClickListener(e -> {
			String encryptedUsername = encryptClientSide(usernameField.getValue(), dynamicKey);
			String encryptedPassword = encryptClientSide(passwordField.getValue(), dynamicKey);
			// String parameter=hiddenField.getValue().trim();
			doLogin(encryptedUsername, encryptedPassword);
		});

		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setSizeFull();

		add(usernameField, passwordField, loginButton);
		getStyle().set("background-color", "hsla(0, 0%, 95%, 0.69)");
	}

	private String generateDynamicKey() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder key = new StringBuilder();
		Random rnd = new Random();
		while (key.length() < 5) { // length of the key
			int index = (int) (rnd.nextFloat() * characters.length());
			key.append(characters.charAt(index));
		}
		return key.toString();
	}

	private Component createPasswordForm() {
		var container = new VerticalLayout();
		container.removeAll();
		Input hiddenField = new Input();
		hiddenField.setValue("login");
		// hiddenField.setAttribute("type", "hidden");
		// TextField hiddenField = new TextField("login", "true");
		hiddenField.setVisible(false);
		// captchacontainer.add(getCaptcha(), refreshButton);
		usernameField.setRequired(true);
		usernameField.setAllowedCharPattern("[0-9A-Za-z@]");
		usernameField.setMinLength(5);
		usernameField.setMaxLength(40);
		passwordField.setRequired(true);
		passwordField.setMinLength(5);
		passwordField.setMaxLength(40);
		captchatext.getElement().setAttribute("autocomplete", "off");
		captchatext.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		captchatext.setPlaceholder("ENTER CAPTCHA");
		captchatext.setMaxLength(6);
		captchatext.setMinLength(6);
		// captchatext.getStyle().set("padding", "20px");

		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.setAutofocus(true);
		anchor.setText("Forgot Password?");
		anchor.getElement().addEventListener("click", e -> ForgotPassword());
		usernameField.getElement().setAttribute("autocomplete", "off");
		passwordField.getElement().setAttribute("autocomplete", "off");

		button.addClickListener(e -> {

			String encryptedUsername = encryptClientSide(usernameField.getValue(), dynamicKey);
			String encryptedPassword = encryptClientSide(passwordField.getValue(), dynamicKey);
			// String parameter=hiddenField.getValue().trim();
			doLogin(encryptedUsername, encryptedPassword);

		});

		anchor.getStyle().set("color", "hsla(119, 93%, 29%, 0.90)").set("padding-bottom", "20px")
				.set("cursor", "pointer").set("text-decoration", "none");

		var form = new FormLayout();
		// form.add(title, 1);
		// form.add(description, 1);
		form.add(usernameField, 1);
		form.add(passwordField, 1);

		form.add(getCaptcha(), 1);
		form.add(new Span(), 1);
		form.add(hiddenField, 1);
		form.add(captchatext, 1);
		// form.add(, 1);
		form.add(button, 1);
		// form.add(anchor, 1);
		form.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("300px", 1));
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

		container.setSizeUndefined();
		container.getStyle().set("background-color", "white");
		// container.getStyle().set("border", "2px solid black");
		container.getStyle().set("border-radius", "10px");
		container.getStyle().set("padding", "0px");
		container.setAlignItems(Alignment.CENTER);
		container.setJustifyContentMode(JustifyContentMode.CENTER);
		container.add(header, form, anchor);
		
		return container;
	}

	private void doLogin(String encryptedUsername, String encryptedPassword) {

		String username = decryptUsername(encryptedUsername, dynamicKey);
		if (captcha.checkUserAnswer(captchatext.getValue())) {
			String password = decryptPassword(encryptedPassword, dynamicKey);

			try {
				invalidatePreviousSessionsForUser(username);
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
				Authentication authentication = this.authenticationManager.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
				context.setAuthentication(authentication);
				this.securityContextHolderStrategy.setContext(context);
				securityRepo.saveContext(context, VaadinServletRequest.getCurrent(),
						VaadinServletResponse.getCurrent());
				registerSession(VaadinService.getCurrentRequest().getWrappedSession(),
						(UserDetails) authentication.getPrincipal());

				audit.saveLoginAudit("Login Successfully", username);

				UI.getCurrent().navigate(HomeView.class);
			} catch (Exception e) {
				audit.saveLoginAudit("Login Failure", username);

				Notification.show("Login failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
				clearFields();
				UI.getCurrent().getPage().reload();
			}
		} else {
			Notification.show("Invalid captcha").addThemeVariants(NotificationVariant.LUMO_ERROR);
			clearFields();
			UI.getCurrent().getPage().reload();
		}

	}

	private String encryptClientSide(String value, String key) {
		// Implement client-side encryption logic here
		return Base64.getEncoder().encodeToString(value.getBytes());
	}

	private String decryptUsername(String encryptedUsername, String key) {
		// Implement server-side decryption logic here
		return new String(Base64.getDecoder().decode(encryptedUsername));
	}

	private String decryptPassword(String encryptedPassword, String key) {
		// Implement server-side decryption logic here
		return new String(Base64.getDecoder().decode(encryptedPassword));
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
					List<SessionInformation> sessionInfoList = sr.getAllSessions(userDetails, true);
					count += sessionInfoList.size();
				}
			}
		}
		return count;
	}

	public void ForgotPassword() {
		aboutdialog.removeAll();
		Button cancelButton = new Button("Cancel");
		H2 headline = new H2("Forgot Password?");
		// H3 header=new H3("Meghalaya Biodiversity Board");
		// H3 header2=new H3("People's Biodiversity Register (PBR): Version 2.0");
		H5 body = new H5("Please Enter Your Email Id");
		captchatext2.getElement().setAttribute("autocomplete", "off");
		captchatext2.setMaxLength(6);
		captchatext2.setPlaceholder("Enter Captcha");
		captchatext2.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		email.getElement().setAttribute("autocomplete", "off");
		email.setPlaceholder("Email");
		email.setMaxLength(40);
		email.setMinLength(5);
		Button submitbutton = new Button("Submit");
		submitbutton.addClickListener(e -> sendPasswordEmail());
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em")
				.set("font-weight", "bold").set("text-decoration", "underline");
		cancelButton.addClickListener(e -> aboutdialog.close());
		HorizontalLayout buttonLayout1 = new HorizontalLayout(submitbutton, cancelButton);
		buttonLayout1.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		var form = new FormLayout();
		form.add(email, 1);
		form.add(getCaptcha2(), 1);
		form.add(new Span(), 1);
		// form.add(hiddenField, 1);
		form.add(captchatext2, 1);
		// form.add(, 1);
		form.add(buttonLayout1, 1);
		// VerticalLayout dialogLayout1 = new VerticalLayout(headline, body, email,
		// getCaptcha(), buttonLayout1);
		VerticalLayout dialogLayout1 = new VerticalLayout(headline, body, form);
		dialogLayout1.setPadding(false);
		dialogLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout1.getStyle().set("width", "300px").set("max-width", "100%");
		aboutdialog.add(dialogLayout1);
		aboutdialog.open();
		// aboutdialog.set
	}

	public Component getCaptcha2() {
		image2 = captcha.getCaptchaImg();
		captchacontainer2.removeAll();
		captchacontainer2.add(image2, refreshButton2);
		refreshButton2.addClickListener(e -> regenerateCaptcha2());
		refreshButton2.setTooltipText("Generate Another Captcha");
		captchacontainer2.setWidthFull();
		captchacontainer2.setJustifyContentMode(JustifyContentMode.CENTER);
		captchacontainer2.getStyle().set("padding", "20px");
		return captchacontainer2;
	}

	private void regenerateCaptcha2() {
		// captchacontainer2.remove(image);
		// captchacontainer2.remove(refreshButton);
		captchacontainer2.removeAll();
		image2 = captcha2.getCaptchaImg();
		captchacontainer2.add(image2, refreshButton2);
	}

	private void sendPasswordEmail() {
		try {

			if (email.getValue().trim().equals("") || captchatext2.getValue().trim().equals("")) {
				Notification.show("Email Id and Captcha is Mandatory").addThemeVariants(NotificationVariant.LUMO_ERROR);
			} else if (captcha2.checkUserAnswer(captchatext2.getValue())) {
				// System.out.println("ABCs");
				UserLogin user = uservice.getUserByEmail(email.getValue());
				if (user == null) {
					Notification.show("Email Id Does Not Exist").addThemeVariants(NotificationVariant.LUMO_ERROR);
				} else {
					// System.out.println("ABC");
					String passwordreset = generateRandomString(8);
					EmailSender.sendEmail(email.getValue(), "MEGPBR", passwordreset);
					user.setHashedPassword(passwordEncoder.encode(passwordreset));
					uservice.update(user);
					aboutdialog.close();
					Notification.show("Your New Password is Generated and Sent to Your Email")
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				}

			} else {
				// System.out.println("Wrong cap");
				Notification.show("Invalid captcha").addThemeVariants(NotificationVariant.LUMO_ERROR);
				email.setValue("");
				captchatext2.setValue("");
				regenerateCaptcha2();
				// clearFields();
				// UI.getCurrent().getPage().reload();
			}
		} catch (Exception e) {
			Notification.show("Invalid captcha").addThemeVariants(NotificationVariant.LUMO_ERROR);
			email.setValue("");
			captchatext2.setValue("");
			regenerateCaptcha2();
		}
	}

	public static String generateRandomString(int length) {
		final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:'\",<.>/?";
		SecureRandom random = new SecureRandom();
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			result.append(CHARACTERS.charAt(randomIndex));
		}

		return result.toString();
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

		regenerateCaptcha();
		button.setEnabled(true);
		passwordField.setValue("");
		usernameField.setValue("");
		captchatext.setValue("");
	}

	public Component getCaptcha() {
		image = captcha.getCaptchaImg();
		// captchacontainer.removeAll();
		captchacontainer.add(image, refreshButton);
		refreshButton.addClickListener(e -> regenerateCaptcha());
		refreshButton.setTooltipText("Generate Another Captcha");
		captchacontainer.setWidthFull();
		captchacontainer.setJustifyContentMode(JustifyContentMode.CENTER);
		captchacontainer.getStyle().set("padding", "10px");
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
		// setCustomCookie();
		if (authenticatedUser.get().isPresent()) {
			// Already logged in
			// loginOverlay.setOpened(false);
			event.forwardTo("/home");
		}

		// loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
}