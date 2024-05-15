package com.megpbr.views.login;



import java.util.List;
import java.util.stream.Stream;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.service.DashboardService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.security.captcha.Captcha;
import com.megpbr.security.captcha.CapthaImpl;
import com.megpbr.views.dashboard.DashboardView;
import com.megpbr.views.dashboard.HierarchicalEntity;
import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Data;
import com.storedobject.chart.DataType;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.Position;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Title;
import com.storedobject.chart.Toolbox;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;


@Route("customlogins")
@PageTitle("Login")
@AnonymousAllowed

public class CustomLoginViewNoCaptcha extends Div implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.LoginEvent> {
//public class LoginViewAudit extends Div implements BeforeEnterObserver {
	@Autowired
	Audit audit;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired SecurityContextRepository securityRepo;
	@Autowired UserDetailsServiceImpl userdetails;
	@Autowired SecurityService secservice;
	@Autowired Dbservice dbservice;
	private DashboardService dservice;
	private final AuthenticatedUser authenticatedUser;
	private static final String LOGIN_BACKGROUND_STYLE = "login";
	FormLayout form = new FormLayout();
    public TextField captchatext=new TextField();
    private static final String LOGIN_SUCCESS_URL = "/";
    TextField code = new TextField("");
    public LoginOverlay loginOverlay = new LoginOverlay();
    //TreeGrid<District> grid=new TreeGrid<>();
    //TreeGrid<Block> treeGrid=new TreeGrid<>();
    //Captcha captcha = new CapthaImpl();
   // District district;
   // private final AuthenticatedUser authenticatedUser;
    public CustomLoginViewNoCaptcha(AuthenticatedUser authenticatedUser, DashboardService dservice, Dbservice dbservice) {
    	this.authenticatedUser=authenticatedUser;
    	this.dservice=dservice;
		this.dbservice = dbservice;
		captchatext.setPlaceholder("ENTER CAPTCHA");
		//Image image = captcha.getCaptchaImg();
    	loginOverlay.setTitle("Meghalaya Biodiversity Board");
    	loginOverlay.setDescription("People's Biodiversity Register Ver. 2.0");
    	//loginOverlay.getCustomFormArea().add(code);
    	//loginOverlay.getCustomFormArea().add(image);
    	//loginOverlay.getCustomFormArea().add(captchatext);
    	loginOverlay.setForgotPasswordButtonVisible(false);
    	loginOverlay.setOpened(true);
    	loginOverlay.addLoginListener(this);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		
	}
    public List<Block> getBlock(District district) {
        return dbservice.getBlocks(district);
    }
   

    

	
	
	
  
	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {

		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(loginEvent.getUsername(),
					loginEvent.getPassword());
			Authentication authenticated = authenticationManager.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authenticated);
			SecurityContext context = SecurityContextHolder.getContext();
			securityRepo.saveContext(context, VaadinServletRequest.getCurrent(), VaadinServletResponse.getCurrent());
			if (authenticated.isAuthenticated()) {
				loginOverlay.setOpened(false);
				audit.saveLoginAudit("Login Successfull", loginEvent.getUsername());
				UI.getCurrent().navigate(DashboardView.class);
			} else {
				// audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
				loginOverlay.setError(true);
			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			loginOverlay.setError(true);
		}

	}
	
	@Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            loginOverlay.setOpened(false);
            event.forwardTo("");
        }

        loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
	
	
}