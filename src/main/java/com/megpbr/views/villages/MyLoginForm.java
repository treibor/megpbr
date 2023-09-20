package com.megpbr.views.villages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.megpbr.audit.CaptchaCheck;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.UserRepository;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
@Route("login1")
@PageTitle("Login")
@AnonymousAllowed

public class MyLoginForm extends VerticalLayout implements BeforeEnterObserver {
	FormLayout form=new FormLayout();
	@Autowired
    UserDetailsServiceImpl ass;
    //@Autowired 
    AuthenticationManager authManager;
    
	//private final AuthenticatedUser authenticatedUser;
    //private LoginForm login = new LoginForm();
    private TextField username=new TextField("User Name");
    private TextField captchatext=new TextField("Enter The Captcha Text Above");
    private PasswordField password=new PasswordField("Password");
    private Button loginbutton=new Button("Login");
    private Button captchabutton=new Button("Login");
    private final UserRepository userRepository;
    private final SecurityService ss;
    private final transient AuthenticationContext authContext;

    public MyLoginForm(AuthenticationContext authContext,UserRepository userRepository, SecurityService ss, AuthenticationManager authManager) {
    	//addClassName("login");
    	this.authManager=authManager;
    	this.authContext=authContext;
    	this.ss=ss;
    	//this.authenticatedUser=authenticatedUser;
    	this.userRepository=userRepository;
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        //login.setAction("dashboard");
        
        add(createForm());
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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public void login() {
    	String x=captchatext.getValue();
    	String y=captchabutton.getText();
    	String usern=username.getValue();
    	String pass=password.getValue();
    	//System.out.println("x="+x );
    	//System.out.println("y="+y );
    	if((x==y)||(x.equals(y))) {
    		//System.out.println("y="+y );
			UserLogin user = userRepository.findByUserName(usern);
			//UserLogin user=ss.getAuthenticatedUser();
			if (user != null && passwordEncoder().matches(pass, user.getHashedPassword())) {
				
			    UI.getCurrent().navigate("login");
				System.out.println("Executed" );
			}else {
				System.out.println("Phaltu" );
			}
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
            //login.setError(true);
        }
    }
}