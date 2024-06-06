package com.megpbr.views.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import com.megpbr.views.dashboard.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import elemental.json.JsonObject;

@Route("login123")
@AnonymousAllowed
public class CryptLogin extends VerticalLayout {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	SecurityContextRepository securityRepo;

	public CryptLogin() {
		TextField usernameField = new TextField("User Name");
		PasswordField passwordField = new PasswordField("Password");
		Button button = new Button("Submit");
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.BETWEEN);
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

		add(usernameField, passwordField, button);
	}

	private void doLogin(String encryptedUsername, String encryptedPassword) {
		String username = CryptUtils.decryptUsername(encryptedUsername);
		String password = CryptUtils.decryptPassword(encryptedPassword);
		//System.out.println("Decrypted:" + username);
		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
			Authentication authenticated = authenticationManager.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authenticated);

			if (authenticated.isAuthenticated()) {
				SecurityContext context = SecurityContextHolder.getContext();
				securityRepo.saveContext(context, VaadinServletRequest.getCurrent(),
						VaadinServletResponse.getCurrent());
				UI.getCurrent().navigate(HomeView.class);
			} else {
				//System.out.println("Decrypted:x");
			}
		} catch (Exception e) {
			//System.out.println("Decrypted:z" );
		}
	}
}