package com.megpbr.views;

import org.vaadin.lineawesome.LineAwesomeIcon;

import com.megpbr.audit.Audit;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.UserService;
import com.megpbr.security.SecurityService;
import com.megpbr.views.agrobiodiversity.CropPlantsView;
import com.megpbr.views.agrobiodiversity.FodderCropsView;
import com.megpbr.views.agrobiodiversity.FruitTreesView;
import com.megpbr.views.agrobiodiversity.MarketsView;
import com.megpbr.views.agrobiodiversity.PeopleScapeView;
import com.megpbr.views.agrobiodiversity.PestCropsView;
import com.megpbr.views.agrobiodiversity.ScapeView;
import com.megpbr.views.agrobiodiversity.SoilTypeView;
import com.megpbr.views.agrobiodiversity.WaterScapeView;
import com.megpbr.views.agrobiodiversity.WeedsView;
import com.megpbr.views.crowd.CrowdView;
import com.megpbr.views.crowd.PreCrowdView;
import com.megpbr.views.dashboard.DashboardView;
import com.megpbr.views.domesticateddiversity.CultureFisheriesView;
import com.megpbr.views.domesticateddiversity.DomFruitTreesView;
import com.megpbr.views.domesticateddiversity.DomesticatedAnimalsView;
import com.megpbr.views.domesticateddiversity.MarketsFairView;
import com.megpbr.views.domesticateddiversity.MedicinalPlantsView;
import com.megpbr.views.domesticateddiversity.OrnamentalPlantsView;
import com.megpbr.views.domesticateddiversity.TimberPlantsView;
import com.megpbr.views.master.MasterView;
import com.megpbr.views.villages.VillageView;
import com.megpbr.views.wilddiversity.AquaticBiodiversityView;
import com.megpbr.views.wilddiversity.FumigateChewingView;
import com.megpbr.views.wilddiversity.TreeShrubView;
import com.megpbr.views.wilddiversity.WildAnimalsView;
import com.megpbr.views.wilddiversity.WildAquaticPlantsView;
import com.megpbr.views.wilddiversity.WildOrnamentalPlantsView;
import com.megpbr.views.wilddiversity.WildPlantSpeciesView;
import com.megpbr.views.wilddiversity.WildPlantsMedicinalView;
import com.megpbr.views.wilddiversity.WildRelativesView;
import com.megpbr.views.wilddiversity.WildTimberPlantsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
	private H2 viewTitle;
    private Dbservice dbservice;
    private UserService userservice;
    private SecurityService securityService;
    private Audit auditobject; 
    public MainLayout(Dbservice dbservice, UserService userservice, SecurityService securityService) {
    	this.dbservice=dbservice;
    	this.securityService=securityService;
    	this.userservice=userservice;
        setPrimarySection(Section.NAVBAR);
        addDrawerContent();
        addHeaderContent();
    }
    public boolean isAdmin() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("ADMIN")) {
			return true;
		}else {
			return false;
		}
    }
    public boolean isStateAdmin() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATEADMIN") || userLevel.endsWith("SUPERADMIN")) {
			return true;
		}else {
			return false;
		}
    }
    public boolean isStateUser() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE")||userLevel.startsWith("SUPER")) {
			return true;
		}else {
			return false;
		}
    }
    public boolean isVerifier() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("VERIFIER")) {
			return true;
		}else {
			return false;
		}
    }
    public boolean isVerifiers() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("VERIFIER")||userLevel.startsWith("SUPER")||userLevel.startsWith("STATE")) {
			return true;
		}else {
			return false;
		}
    }
    private void addHeaderContent() {
    	String user=userservice.getLoggedUserName();
    	Avatar avatarImage = new Avatar(user);
    	//avatarImage.addThemeVariants(AvatarVariant.LUMO_SMALL);
		avatarImage.setColorIndex(5);
		avatarImage.setTooltipEnabled(true);
		MenuBar menuBar = new MenuBar();
		menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem item = menuBar.addItem(avatarImage);
		SubMenu subMenu = item.getSubMenu();
		subMenu.addItem("About", e->createAboutSection());
		subMenu.addItem("Change Password", e->changePassword());
		subMenu.addItem("Create User",e->createUser()).setVisible(isAdmin());
		subMenu.addItem("Logout", e -> securityService.logout());
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
        if (securityService.getAuthenticatedUser() != null) {
            header .add(img, headerText, menuBar);
            header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            header.expand(headerText);
            header.setWidthFull();
        } else {
            //header = new HorizontalLayout(logo);
        }
        addToNavbar(true, toggle,header);
    }
   
   
	private void addDrawerContent() {
        H1 appName = new H1("Hi, "+ userservice.getLoggedUserName());
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        //appName.add
        Header header = new Header(appName);
        //Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, createNavigation(), createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.ACCUSOFT.create()));
        SideNavItem village=new SideNavItem("Villages Details", VillageView.class, LineAwesomeIcon.AVIANEX.create());
        nav.addItem(village);
        //Agro Biodiversity -Category 1
        SideNavItem category1=new SideNavItem(dbservice.getCategory(1).getCategory());
		category1.setPrefixComponent(LineAwesomeIcon.ACQUISITIONS_INCORPORATED.create());
		SideNavItem format1 = new SideNavItem(dbservice.getFormat(1).getFormatName(), CropPlantsView.class,
				LineAwesomeIcon.AFFILIATETHEME.create());
		SideNavItem format2 = new SideNavItem(dbservice.getFormat(2).getFormatName(), FruitTreesView.class,
				LineAwesomeIcon.ALGOLIA.create());
		SideNavItem format3 = new SideNavItem(dbservice.getFormat(3).getFormatName(), FodderCropsView.class,
				LineAwesomeIcon.ALIPAY.create());
		SideNavItem format4 = new SideNavItem(dbservice.getFormat(4).getFormatName(), WeedsView.class,
				LineAwesomeIcon.AUDIBLE.create());
		SideNavItem format5 = new SideNavItem(dbservice.getFormat(5).getFormatName(), PestCropsView.class,
				LineAwesomeIcon.ANGULAR.create());
		
		SideNavItem format6 = new SideNavItem(dbservice.getFormat(6).getFormatName(), MarketsView.class,
				LineAwesomeIcon.MEGAPORT.create());
		SideNavItem format7 = new SideNavItem(dbservice.getFormat(7).getFormatName(), PeopleScapeView.class,
				LineAwesomeIcon.MANDALORIAN.create());
		SideNavItem format8 = new SideNavItem(dbservice.getFormat(8).getFormatName(), ScapeView.class,
				LineAwesomeIcon.LANDMARK_SOLID.create());
		SideNavItem format9 = new SideNavItem(dbservice.getFormat(9).getFormatName(), WaterScapeView.class,
				LineAwesomeIcon.MAGENTO.create());
		SideNavItem format10 = new SideNavItem(dbservice.getFormat(10).getFormatName(), SoilTypeView.class,
				LineAwesomeIcon.MARS_SOLID.create());
		category1.addItem(format1,format2, format3,format4,format5,format6, format7, format8, format9, format10);
		//Domesticated Biodiversity -Category 2
        SideNavItem category2=new SideNavItem(dbservice.getCategory(2).getCategory());
        category2.setPrefixComponent(LineAwesomeIcon.AIRBNB.create());
        SideNavItem format11 = new SideNavItem(dbservice.getFormat(11).getFormatName(), DomFruitTreesView.class,
				LineAwesomeIcon.ZHIHU.create());
        SideNavItem format12 = new SideNavItem(dbservice.getFormat(12).getFormatName(), MedicinalPlantsView.class,
				LineAwesomeIcon.YOAST.create());
        SideNavItem format13 = new SideNavItem(dbservice.getFormat(13).getFormatName(), OrnamentalPlantsView.class,
				LineAwesomeIcon.YELP.create());
        SideNavItem format14 = new SideNavItem(dbservice.getFormat(14).getFormatName(), TimberPlantsView.class,
				LineAwesomeIcon.SKYATLAS.create());
        SideNavItem format15 = new SideNavItem(dbservice.getFormat(15).getFormatName(), DomesticatedAnimalsView.class,
				LineAwesomeIcon.THE_RED_YETI.create());
        SideNavItem format16 = new SideNavItem(dbservice.getFormat(16).getFormatName(), CultureFisheriesView.class,
				LineAwesomeIcon.FISH_SOLID.create());
		SideNavItem format17 = new SideNavItem(dbservice.getFormat(17).getFormatName(), MarketsFairView.class,
				LineAwesomeIcon.AMILIA.create());
		category2.addItem(format11, format12, format13, format14, format15, format16, format17);
		
		//Wild Biodiversity -Category 3
		SideNavItem category3=new SideNavItem(dbservice.getCategory(3).getCategory());
		category3.setPrefixComponent(LineAwesomeIcon.AIR_FRESHENER_SOLID.create());
		SideNavItem format18 = new SideNavItem(dbservice.getFormat(18).getFormatName(), TreeShrubView.class,
				LineAwesomeIcon.ALGOLIA.create());
		SideNavItem format19 = new SideNavItem(dbservice.getFormat(19).getFormatName(), WildPlantSpeciesView.class,
				LineAwesomeIcon.ARCHWAY_SOLID.create());
		SideNavItem format20 = new SideNavItem(dbservice.getFormat(20).getFormatName(), AquaticBiodiversityView.class,
				LineAwesomeIcon.BATH_SOLID.create());
		SideNavItem format21 = new SideNavItem(dbservice.getFormat(21).getFormatName(), WildAquaticPlantsView.class,
				LineAwesomeIcon.BANDCAMP.create());
		SideNavItem format22 = new SideNavItem(dbservice.getFormat(22).getFormatName(), WildPlantsMedicinalView.class,
				LineAwesomeIcon.BELL.create());
		SideNavItem format23 = new SideNavItem(dbservice.getFormat(23).getFormatName(), WildRelativesView.class,
				LineAwesomeIcon.BEZIER_CURVE_SOLID.create());
		SideNavItem format24 = new SideNavItem(dbservice.getFormat(24).getFormatName(), WildOrnamentalPlantsView.class,
				LineAwesomeIcon.BLACKBERRY.create());
		SideNavItem format25 = new SideNavItem(dbservice.getFormat(25).getFormatName(), FumigateChewingView.class,
				LineAwesomeIcon.FEATHER_ALT_SOLID.create());
		SideNavItem format26 = new SideNavItem(dbservice.getFormat(26).getFormatName(), WildTimberPlantsView.class,
				LineAwesomeIcon.CAMPGROUND_SOLID.create());
		SideNavItem format27 = new SideNavItem(dbservice.getFormat(27).getFormatName(), WildAnimalsView.class,
				LineAwesomeIcon.WOLF_PACK_BATTALION.create());
        category3.addItem(format18,format19,format20,format21,format22,format23,format24,format25,format26,format27);
        nav.addItem(category1, category2, category3);
        SideNavItem crowd=new SideNavItem("Crowd Sourcing");
        crowd.setPrefixComponent(LineAwesomeIcon.PEOPLE_CARRY_SOLID.create());
        SideNavItem preverify=new SideNavItem("Verification", PreCrowdView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        SideNavItem verify=new SideNavItem("Verification", CrowdView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        crowd.addItem(preverify, verify);
        nav.addItem(crowd);
        SideNavItem master=new SideNavItem("Master", MasterView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        nav.addItem(master);
        crowd.setVisible(isVerifiers());
        master.setVisible(isStateAdmin());
        preverify.setVisible(isStateUser());
        verify.setVisible(isVerifier());
        category1.setVisible(!isVerifier());
        category2.setVisible(!isVerifier());
        category3.setVisible(!isVerifier());
        village.setVisible(!isVerifier());
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
    private void createAboutSection() {
		// TODO Auto-generated method stub
    	final UserView user=new UserView(dbservice, userservice);
		user.createAbout();
	}
	
	public void createUser() {
		final UserView user=new UserView(dbservice, userservice);
		user.createUser();
	}
	public void changePassword() {
		final UserView user=new UserView(dbservice, userservice);
		user.changePassword();
	}
}
















