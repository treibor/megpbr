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
@Route("login1")
@PageTitle("Login")
@AnonymousAllowed

@Tag("form")
public class MyLoginForm extends HtmlContainer implements BeforeEnterObserver{
	TextField captcha = new TextField("Captcha");
   
    	public MyLoginForm() {
            addClassNames(LumoUtility.Display.FLEX,
                    LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START);
            TextField username = new TextField("Username");
            PasswordField password = new PasswordField("Password");
            Button login = new Button("Login");
            login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            login.getElement().setAttribute("on-click", "submit");
            getElement().setAttribute("method", "POST");
            getElement().setAttribute("action", "login");
           login.addClickListener(e->login());
            add(username, password, login);
        }
       
    
    public void login() {
    	 
    }

	private void doSome() {
		// TODO Auto-generated method stub
		System.out.println("ABC");
		
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