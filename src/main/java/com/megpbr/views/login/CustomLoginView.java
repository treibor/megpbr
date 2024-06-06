package com.megpbr.views.login;

import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.SecurityContextRepository;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.service.DashboardService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.security.SecurityService;
import com.megpbr.security.UserDetailsServiceImpl;
import com.megpbr.security.captcha.Captcha;
import com.megpbr.security.captcha.CapthaImpl;
import com.megpbr.views.dashboard.HomeView;
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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.auth.AnonymousAllowed;
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

@Route("login")
@PageTitle("Login")
@AnonymousAllowed

public class CustomLoginView extends Div
		implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.LoginEvent> {
//public class LoginViewAudit extends Div implements BeforeEnterObserver {
	@Autowired
	Audit audit;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	SecurityContextRepository securityRepo;
	@Autowired
	UserDetailsServiceImpl userdetails;
	@Autowired
	SecurityService secservice;
	@Autowired
	Dbservice dbservice;

	@Autowired
	private SessionRegistry sessionRegistry;
	private DashboardService dservice;
	private final AuthenticatedUser authenticatedUser;
	private static final String LOGIN_BACKGROUND_STYLE = "login";
	FormLayout form = new FormLayout();
	public TextField captchatext = new TextField();
	private Button loginbutton = new Button("Login");
	// private Button refresfbutton=new Button("Login");
	private static final String LOGIN_SUCCESS_URL = "/";
	TextField code = new TextField("");
	Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
	public LoginOverlay loginOverlay = new LoginOverlay();
	Captcha captcha = new CapthaImpl();
	Image image;
	HorizontalLayout captchacontainer = new HorizontalLayout();

	public CustomLoginView(AuthenticatedUser authenticatedUser, DashboardService dservice, Dbservice dbservice) {
		this.authenticatedUser = authenticatedUser;
		this.dservice = dservice;
		this.dbservice = dbservice;
		captchatext.setPlaceholder("ENTER CAPTCHA");

		loginOverlay.setTitle("Meghalaya Biodiversity Board");
		loginOverlay.setDescription("People's Biodiversity Register Ver. 2.0");
		// loginOverlay.getCustomFormArea().add(code);
		loginOverlay.getCustomFormArea().add(getCaptcha());
		loginOverlay.getCustomFormArea().add(captchatext);
		loginOverlay.setForgotPasswordButtonVisible(false);

		// add(createHeaderContent());
		loginOverlay.setOpened(true);
		loginbutton.addClickListener(e -> loginOverlay.setOpened(true));
		loginOverlay.addLoginListener(this);
		loginOverlay.getElement().setAttribute("no-autofocus", "");

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
		// TODO Auto-generated method stub
		captchacontainer.remove(image);
		captchacontainer.remove(refreshButton);
		image = captcha.getCaptchaImg();
		captchacontainer.add(image, refreshButton);
	}
	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
	    if (captcha.checkUserAnswer(captchatext.getValue())) {
	        try {
	            Authentication authentication = new UsernamePasswordAuthenticationToken(
	                loginEvent.getUsername(),
	                loginEvent.getPassword()
	            );
	            Authentication authenticated = authenticationManager.authenticate(authentication);
	            SecurityContextHolder.getContext().setAuthentication(authenticated);

	            if (authenticated.isAuthenticated()) {
	                loginOverlay.setOpened(false);
	                audit.saveLoginAudit("Login Successful", loginEvent.getUsername());
	                // Save the SecurityContext to the SecurityContextRepository
	                SecurityContext context = SecurityContextHolder.getContext();
	                securityRepo.saveContext(context, VaadinServletRequest.getCurrent(), VaadinServletResponse.getCurrent());
	                UI.getCurrent().navigate(HomeView.class);
	            } else {
	            	
	                loginOverlay.setError(true);
	                
	            }
	        } catch (Exception e) {
	            audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
	            loginOverlay.setError(true);
	        }
	    } else {
	        Notification.show("Please Enter The Correct Captcha Text").addThemeVariants(NotificationVariant.LUMO_ERROR);
	        loginOverlay.setEnabled(true);
	    }
	}
	
	





	
//	@Override
//	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
//	    if (captcha.checkUserAnswer(captchatext.getValue())) {
//	        try {
//	            Authentication authentication = new UsernamePasswordAuthenticationToken(
//	                loginEvent.getUsername(),
//	                loginEvent.getPassword()
//	            );
//	            Authentication authenticated = authenticationManager.authenticate(authentication);
//	            SecurityContextHolder.getContext().setAuthentication(authenticated);
//	            SecurityContext context = SecurityContextHolder.getContext();
//	            securityRepo.saveContext(context, VaadinServletRequest.getCurrent(), VaadinServletResponse.getCurrent());
//	            
//	            if (authenticated.isAuthenticated()) {
//	                loginOverlay.setOpened(false);
//	                audit.saveLoginAudit("Login Successful", loginEvent.getUsername());
//	                UI.getCurrent().navigate(HomeView.class);
//	            } else {
//	                // audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
//	                loginOverlay.setError(true);
//	            }
//	        } catch (Exception e) {
//	            // TODO Auto-generated catch block
//	            // System.out.println("Login Failuresss");
//	            // e.printStackTrace();
//	            audit.saveLoginAudit("Login Failure", loginEvent.getUsername());
//	            loginOverlay.setError(true);
//	        }
//	    } else {
//	        Notification.show("Please Enter The Correct Captcha Text").addThemeVariants(NotificationVariant.LUMO_ERROR);
//	        loginOverlay.setEnabled(true);
//	    }
//	}

	public Component getCharts3() {
		SOChart soChartf = new SOChart();
		// SOChart soChartf = new SOChart();
		CategoryData labels = new CategoryData();
		Data data = new Data();
		int i = dservice.getFormats().size();
		// System.out.println("Formats "+i);
		for (int index = 0; index < i; index++) {
			labels.add(dservice.getFormats().get(index).getFormat() + " - "
					+ dservice.getFormats().get(index).getFormatName());
			// District dist=dservice.getDistricts().get(index);
			MasterFormat format = dservice.getFormats().get(index);
			data.add(dservice.getFormatCount(format, false));
		}
		BarChart bc = new BarChart(labels, data);

		RectangularCoordinate rc;
		rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
		Position p = new Position();
		bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system
		bc.setName("PBR Entered");
		Toolbox toolbox = new Toolbox();
		toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
		Title title = new Title("Format Wise PBR");

		NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
		nc.setName("PBR Entered");
		nc.setPosition(p); // Position it leaving 50% space at the top
		// soChartf.add(nc, toolbox);
		soChartf.add(bc, toolbox, title);
		HorizontalLayout getCharts = new HorizontalLayout();
		// getCharts.
		getCharts.addClassName("chartsLayout1");
		getCharts.setWidthFull();
		// getCharts.setHeight("10px");
		soChartf.setWidthFull();
		getCharts.add(soChartf);
		return soChartf;
	}

	private Component createHeaderContent() {
		// Button but=new Button("Login");
		loginbutton.addClickListener(e -> loginOverlay.setOpened(true));
		Header header = new Header();
		header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

		Div layout = new Div();
		layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

		H1 appName = new H1("Unauthenticated");
		appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
		layout.add(appName, loginbutton);

		Nav nav = new Nav();
		nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

		// Wrap the links in a list; improves accessibility
		UnorderedList list = new UnorderedList();
		list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
		nav.add(list);

		/*
		 * for (MenuItemInfo menuItem : createMenuItems()) { list.add(menuItem);
		 * 
		 * }
		 */

		header.add(layout, nav);
		return header;
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