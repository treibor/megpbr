package com.megpbr.views.villages;

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

import com.megpbr.audit.CaptchaCheck;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
	FormLayout form=new FormLayout();
    //private LoginForm login = new LoginForm();
    private TextField username=new TextField("User Name");
    public TextField captchatext=new TextField("Enter The Captcha Text Above");
    private PasswordField password=new PasswordField("Password");
    private Button loginbutton=new Button("Login");
    public Button captchabutton=new Button("Login");
    //private final AuthenticatedUser authenticatedUser;
    //MyLoginForm login=new MyLoginForm();
	public LoginView() {
		// addClassName("login");
		//this.authenticatedUser = authenticatedUser;
		
		  LoginI18n i18n = LoginI18n.createDefault(); i18n.setHeader(new
		  LoginI18n.Header()); i18n.getHeader().setTitle("MEGPBR");
		  i18n.getHeader().setDescription(""); i18n.setAdditionalInformation(null);
		  setI18n(i18n); 
		  addCaptchaText(); 
		  //addCustomComponent(captchatext);
		  //addCaptcha();
		  setForgotPasswordButtonVisible(false); 
		  setOpened(true);
		  
		 setAction("login");
		//login.setAction("logincheck");
		//add(login);
	}
    

    public void addCaptcha() {
    	CaptchaCheck captcha = new CaptchaCheck();
		String a=captcha.generateCaptcha(5);
		captchabutton=new Button(a);
		captchabutton.setEnabled(false);
		captchabutton.setHeight("50px");
        captchabutton.getElement().setAttribute("name", "remember-me");
        //TextField captchaText=new TextField("Enter Captcha");
        Element loginFormElement = getElement();
        Element element = captchabutton.getElement();
        //Element element2 = captchaText.getElement();
        loginFormElement.appendChild(element);
        //loginFormElement.appendChild(element2);
        
        String executeJsForFieldString = "const field = document.getElementById($0);" +
              "if(field) {" +"   field.after($1)" +"} else {" +"   console.error('could not find field', $0);" +"}";
        getElement().executeJs(executeJsForFieldString, "vaadinLoginPassword", element);
    }
    public void addCaptchaText() {
    	TextField captchaText=new TextField("Enter Captcha");
    	captchaText.setRequired(true);
        Element loginFormElement = getElement();
        //Element element = captchabutton.getElement();
        Element element = captchaText.getElement();
        //loginFormElement.appendChild(element);
        loginFormElement.appendChild(element);
        
        String executeJsForFieldString = "const field = document.getElementById($0);" +
                "if(field) {" +
                "   field.after($1)" +
                "} else {" +
                "   console.error('could not find field', $0);" +
                "}";
        getElement().executeJs(executeJsForFieldString, "vaadinLoginPassword", element);
    }
    public void addCustomComponent(Component... pComponents) {
        Element loginFormElement = getElement();
        
        Element[] elements = Arrays.stream(pComponents).map(Component::getElement).toArray(Element[]::new);
        loginFormElement.appendChild(elements); // to have them in the DOM

        String executeJsForFieldString = "let fields = this.getElementsByTagName($0);" +
                "if(fields && fields[0] && fields[0].id === $1) {" +
                "   fields[0].after($2)" +
                "} else {" +
                "   console.error('could not find field', $0, $1);" +
                "}";

        // adding them in reverse to simply reuse the same execution string. you could also reuse the previously added element for the "after" call
        for (int i = elements.length - 1; i >= 0; i--) {
            loginFormElement.executeJs(executeJsForFieldString, "vaadin-password-field", "vaadinLoginPassword", elements[i]);
        }
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
    /*
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }*/
    
   
}