package com.megpbr.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.megpbr.audit.Audit;
import com.megpbr.audit.CaptchaCheck;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.security.SecurityUtils;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("login")
@PageTitle("Login")
@AnonymousAllowed

public class LoginView extends Div implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.LoginEvent> {
	@Autowired
	Audit audit;
	FormLayout form=new FormLayout();
    //private LoginForm login = new LoginForm();
    private TextField username=new TextField("User Name");
    public TextField captchatext=new TextField();
    private PasswordField password=new PasswordField("Password");
    private Button loginbutton=new Button("Login");
    //public Button captchabutton=new Button("");
    //private final AuthenticatedUser authenticatedUser;
    private static final String LOGIN_SUCCESS_URL = "/";
    TextField code = new TextField("");
    LoginOverlay loginOverlay = new LoginOverlay();
    //private static CaptchaCheck captcha;
	public LoginView() {
		refreshCaptcha();
		captchatext.setEnabled(false);
		captchatext.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		captchatext.setWidthFull();
		captchatext.getStyle().set("font-size", "15px");
		captchatext.getStyle().set("font-style", "oblique");
		Button captchabutton = new Button(new Icon(VaadinIcon.REFRESH));
		captchabutton.addClickListener(e-> refreshCaptcha());
		var captchapanel=new HorizontalLayout(captchatext, captchabutton);
		captchapanel.setAlignItems(Alignment.BASELINE);
		captchapanel.setWidthFull();
		code.setPlaceholder("Enter Captcha Text");
		code.getElement().setAttribute("name", "code");
		code.setErrorMessage("Required");
		code.setRequired(true);
		loginOverlay.getCustomFormArea().add(captchapanel);
		loginOverlay.getCustomFormArea().add(code);
		add(loginOverlay);
		loginOverlay.setTitle("Meghalaya Biodiversity Board");
		loginOverlay.setDescription("People's Biodiversity Register");
		loginOverlay.setForgotPasswordButtonVisible(false);
		loginOverlay.setOpened(true);
		//loginOverlay.setError(true);
		loginOverlay.addLoginListener(this);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
	}
    
	public void refreshCaptcha() {
		captchatext.setValue(CaptchaCheck.generateCaptcha(5));
    }

   
   
	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
		String codevalue=code.getValue().trim();
		String captchavalue=captchatext.getValue().trim();
		if (codevalue.equals(captchavalue)) {
			inValidateSession();
			boolean authenticated = SecurityUtils.authentication(loginEvent.getUsername(), loginEvent.getPassword());
			if (authenticated) {
				UI.getCurrent().getPage().setLocation(LOGIN_SUCCESS_URL);
				audit.saveAudit("Login Successfull", loginEvent.getUsername());
			} else {
				audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
				loginOverlay.setError(true);
				refreshCaptcha();
			}
		} else {
			Notification.show("Invalid Captcha Text");
			loginOverlay.setError(true);
			refreshCaptcha();
			loginOverlay.setEnabled(true);
		}
	}
    public void inValidateSession(){
    	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
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
    
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        if (authenticatedUser.get().isPresent()) {
//            // Already logged in
//            setOpened(false);
//            event.forwardTo("");
//        }
//
//        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
//    }
    
   
}