package com.megpbr.views.villages;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;

import com.megpbr.audit.CaptchaCheck;
import com.megpbr.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.server.auth.AnonymousAllowed;

import nl.captcha.Captcha;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	FormLayout form=new FormLayout();
    private LoginForm login = new LoginForm();
    private TextField username=new TextField("User Name");
    private TextField captchatext=new TextField("Enter The Captcha Text Above");
    private PasswordField password=new PasswordField("Password");
    private Button loginbutton=new Button("Login");
    private Button captchabutton=new Button("Login");
    public LoginView() {
    	//addClassName("login");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        login.setAction("dashboard");
        
        add(login,createForm());
    }

    private Component createCaptcha() {
		CaptchaCheck captcha = new CaptchaCheck();
		String a=captcha.generateCaptcha(5);
		captchabutton=new Button(a);
		//captchabutton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		captchabutton.setEnabled(false);
		return(captchabutton);
    }
    private Component createForm() {
    	loginbutton.addClickListener(e->login());
    	
    	form.add(username,2);
    	form.add(password, 2);
    	form.add(createCaptcha(), 1);
    	form.add(captchatext, 1);
    	form.add(loginbutton, 2);
    	form.setResponsiveSteps(new ResponsiveStep("0", 2),new ResponsiveStep("500px", 2));
    	var container=new VerticalLayout();
    	container.setWidth("20%");
    	container.add(new H1("MEGPBR"),form);
    	return container;
    }
    
    public void login() {
    	String x=captchatext.getValue();
    	String y=captchabutton.getText();
    	//System.out.println("x="+x );
    	//System.out.println("y="+y );
    	if((x==y)||(x.equals(y))) {
    		System.out.println("y="+y );
    		UI.getCurrent().navigate(DashboardView.class);
    	}else {
    		System.out.println("x!=y" );
    	}
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            login.setError(true);
        }
    }
}