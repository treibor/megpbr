package com.megpbr.views.unauthenticated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.megpbr.audit.Audit;
import com.megpbr.security.AuthenticatedUser;
import com.megpbr.views.dashboard.DashboardView;
import com.megpbr.views.login.CustomLoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.theme.lumo.LumoUtility;
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

/**
 * The main view is a top-level placeholder for other views.
 */

public class MainLayout extends AppLayout {
	private H2 viewTitle;
    private Audit auditobject; 
    AuthenticatedUser authUser;
    CustomLoginView custom;
    public MainLayout(AuthenticatedUser authenticatedUser) {
    	this.authUser=authenticatedUser;
    	//custom=new CustomLoginView(authUser);
    	//loginOverlay.setAction("logins");
    	//add(login);
    	setPrimarySection(Section.DRAWER);
    	addToNavbar(createHeaderContent());
        //addDrawerContent();
        //addHeaderContent();
        
        //System.out.println("A:"+auth.getPrincipalName());
    }
   
    private Component createHeaderContent() {
    	 Button but=new Button("Login");
         but.addClickListener(e->custom.loginOverlay.setOpened(true));
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("Unauthenticated");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName, but);

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
       
        header.add(layout, nav, custom.loginOverlay);
        return header;
    }
    private void addHeaderContent() {
    	//UserLogin logUser=userservice.get
    	
    	Avatar avatarImage = new Avatar("OLA");
    	//avatarImage.addThemeVariants(AvatarVariant.LUMO_SMALL);
		avatarImage.setColorIndex(5);
		avatarImage.setTooltipEnabled(true);
		MenuBar menuBar = new MenuBar();
		menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem item = menuBar.addItem(avatarImage);
		SubMenu subMenu = item.getSubMenu();
		
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        Image img = new Image("images/emblem-dark.png", "placeholder plant");
        img.setWidth("60px");
        img.setHeight("100px");
        var header=new HorizontalLayout();
        
        var viewTitle1 = new H6("Government of Meghalaya");
        viewTitle = new H2("MEGHALAYA BIODIVERSITY BOARD");
        viewTitle.getStyle().set("font-size", "20px");
        var viewTitle2 = new H6("People's Biodiversity Register");
        var headerText=new VerticalLayout(viewTitle1, viewTitle, viewTitle2);
        //header.add(img, new VerticalLayout());
        
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        header.addClassNames(Margin.Top.SMALL, Margin.Bottom.SMALL);
        header.addClassName(Padding.Left.XLARGE);
        
            header .add(img, headerText, menuBar);
            header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            header.expand(headerText);
            header.setWidthFull();
        addToNavbar(true, toggle,header);
    }

	
   
	private void addDrawerContent() {
        H1 appName = new H1("Hi, ");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        
        Header header = new Header(appName);
        //Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, createNavigation(), createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.ACCUSOFT.create()));
        
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        //viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
   
	
}
















