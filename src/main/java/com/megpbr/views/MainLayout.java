package com.megpbr.views;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.UserLogin;
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
import com.megpbr.views.dashboard.HomeView;
import com.megpbr.views.dashboard.StatsView;
import com.megpbr.views.dashboard.YearDashboardView;
import com.megpbr.views.domesticateddiversity.CultureFisheriesView;
import com.megpbr.views.domesticateddiversity.DomFruitTreesView;
import com.megpbr.views.domesticateddiversity.DomesticatedAnimalsView;
import com.megpbr.views.domesticateddiversity.MarketsFairView;
import com.megpbr.views.domesticateddiversity.MedicinalPlantsView;
import com.megpbr.views.domesticateddiversity.OrnamentalPlantsView;
import com.megpbr.views.domesticateddiversity.TimberPlantsView;
import com.megpbr.views.master.MasterView;
import com.megpbr.views.master.lgd;
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
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

/**
 * The main view is a top-level placeholder for other views.
 */

public class MainLayout extends AppLayout {
	//private H2 viewTitle;
    private Dbservice dbservice;
    private UserService userservice;
    private SecurityService securityService;
    @Autowired private Audit auditobject; 
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Dialog dialog;
    PasswordField oldpwd;
	PasswordField newpwd;
	PasswordField confirmpwd;
	Button cancelButton = new Button("Cancel");
	Button saveButton = new Button("Save");
    public MainLayout(Dbservice dbservice, UserService userservice, SecurityService securityService) {
    	redirectOnError();
    	this.dbservice=dbservice;
    	this.securityService=securityService;
    	this.userservice=userservice;
    	//this.authContext=auth;
        setPrimarySection(Section.NAVBAR);
        addDrawerContent();
        addHeaderContent();
        checkPasswordExpiry();
        //System.out.println("A:"+auth.getPrincipalName());
    }
    private void checkPasswordExpiry() {
		UserLogin user = userservice.getLoggedUser();
		LocalDate expiryDate = user.getPwdChangedDate();
		LocalDate expiryDatePlus180Days = expiryDate.plus(180, ChronoUnit.DAYS);
		LocalDate today = LocalDate.now();
		boolean isExpiryDateValid = expiryDatePlus180Days.isAfter(today);
		if (!isExpiryDateValid) {
			openMandatoryPasswordDialog();
		}
	}
    private void openMandatoryPasswordDialog() {
		if (dialog != null) {
			dialog = null;
		}
		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		//VerticalLayout dialogLayout = createDialogLayout(dialog);
		H2 headline = new H2("Password Expired - Change Password");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold");
		//newpwd.setMinLength(0);
		oldpwd = new PasswordField("Old Password");
		newpwd = new PasswordField("New Password");
		confirmpwd = new PasswordField("Confirm New Password");
		oldpwd.setRevealButtonVisible(false);
		newpwd.setRevealButtonVisible(false);
		confirmpwd.setRevealButtonVisible(false);
		// oldpwd.setValue("");
		//cancelButton.setText(//userType);
		cancelButton.addClickListener(e -> securityService.logout());
		saveButton.addClickListener(e -> forcechangePassword());
		VerticalLayout fieldLayout = new VerticalLayout(oldpwd, newpwd, confirmpwd);
		fieldLayout.setSpacing(false);
		fieldLayout.setPadding(false);
		fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
		buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
		
		dialog.add(dialogLayout);
		dialog.open();
	}
    private boolean checkPasswordStrength(String password) {
		boolean containsLowerChar= false, containsUpperChar = false;
		boolean containsDigit = false, containsSpecialChar = false;
		char[] ch= password.toCharArray();
		//System.out.println(password);
		String special_chars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";
		for (int i = 0; i < password.length(); i++) {
			if (Character.isLowerCase(ch[i])) {
				containsLowerChar= true;
			}	
			if (Character.isUpperCase(ch[i])) {
				containsUpperChar= true;
			}
			if (Character.isDigit(ch[i])) {
				containsDigit= true;
			}
			if (special_chars.contains(String.valueOf(ch[i]))) {
				containsSpecialChar=true;
			}
		}
		if(containsDigit && containsUpperChar && containsSpecialChar && containsLowerChar){
			return true;
		}
		return false;
	}
    private void forcechangePassword() {
		// notify.show("Under Development", 3000, Position.TOP_CENTER);
		if (oldpwd.getValue() == "" || newpwd.getValue() == "" || confirmpwd.getValue() == "") {
			Notification.show("Error: Enter All Values, Please", 3000, Position.TOP_CENTER);
		} else if(!checkPasswordStrength(newpwd.getValue())){
			Notification.show("Password is too weak. Please use a combination of Lower case, Upper case, Number and Special Charaters").addThemeVariants(NotificationVariant.LUMO_WARNING);
		} else {
			if (newpwd.getValue().trim().equals(confirmpwd.getValue().trim())) {
				String pwd = oldpwd.getValue();

				if (passwordEncoder.matches(pwd, userservice.getLoggedUser().getHashedPassword())) {
					UserLogin user = userservice.getLoggedUser();
					user.setHashedPassword(passwordEncoder.encode(newpwd.getValue().trim()));
					user.setPwdChangedDate(LocalDate.now());
					userservice.update(user);
					showConfirmationDialog();

				} else {

					Notification.show("Unauthorised User", 3000, Position.TOP_CENTER);
				}
			} else {
				Notification.show("Please check and confirm your passwords", 3000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		}
	}
    private void showConfirmationDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Password Changed");
        dialog.setText("Password has been successfully changed. You will be now be logged out. Please Login again with your new Password");
        
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> {
            securityService.logout(); // Call the logout method
            //getUI().ifPresent(ui -> ui.navigate("login")); // Redirect to the login page
        });

        dialog.open(); // Open the dialog
    }
    public void redirectOnError(){
    	UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(event -> {
            VaadinSession vaadinSession = VaadinSession.getCurrent();
            if (vaadinSession == null || vaadinSession.getSession() == null) {
                // Suppress error handling and redirect to login
                UI.getCurrent().getPage().setLocation("/login");
            }
        });
    	
    }
    public boolean isAdmin() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("ADMIN")) {
			return true;
		}else {
			return false;
		}
    }
    public boolean isSuperAdmin() {
    	String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("SUPERADMIN")) {
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
    public boolean isVerifierAndSuper() {
    	String userLevel=userservice.getLoggedUserLevel();
    	
		if(userLevel.endsWith("VERIFIER")||userLevel.startsWith("SUPER")) {
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
    	//UserLogin logUser=userservice.get
    	String user=userservice.getLoggedUserName();
    	Avatar avatarImage = new Avatar(user);
    	//avatarImage.addThemeVariants(AvatarVariant.LUMO_SMALL);
		avatarImage.setColorIndex(5);
		avatarImage.setTooltipEnabled(true);
		MenuBar menuBar = new MenuBar();
		menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem item = menuBar.addItem(avatarImage);
		SubMenu subMenu = item.getSubMenu();
		//createIconItem(subMenu, VaadinIcon.SHARE, "By Email", null, true);
		subMenu.addItem("About", e->createAboutSection());
		//a.add(LineAwesomeIcon.ADDRESS_BOOK.create());
		subMenu.addItem("Change Password", e->changePassword());
		subMenu.addItem("Create User",e->createUser()).setVisible(isAdmin());
		subMenu.addItem("Logout", e -> logout());
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        Image img = new Image("images/emblem-dark.png", "placeholder plant");
        img.setWidth("50px");
        img.setHeight("80px");
        var header=new HorizontalLayout();
        
        var viewTitle1 = new H6("Government of Meghalaya");
        H4 viewTitle = new H4("MEGHALAYA BIODIVERSITY BOARD");
        viewTitle.getStyle().set("font-size", "20px");
        //viewTitle.getStyle().set("background-color", "hsla(119, 93%, 29%, 0.90)");
        var viewTitle2 = new H6("People's Biodiversity Register");
        viewTitle2.getStyle().set("color", "hsla(119, 93%, 29%, 0.90)");
        var headerText=new VerticalLayout(viewTitle1, viewTitle, viewTitle2);
        //header.add(img, new VerticalLayout());
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        viewTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);
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
        H1 appName = new H1("Hi, "+ userservice.getLoggedUser().getName());
        appName.getStyle().set("color", "hsla(119, 93%, 29%, 0.90)").set("padding-bottom", "20px");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        //appName.add
        Header header = new Header(appName);
        //Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, createNavigation(), createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        SideNavItem dashboardItems=new SideNavItem("Dashboard");
        dashboardItems.setPrefixComponent(LineAwesomeIcon.ACCUSOFT.create());
        SideNavItem dashboardItem=new SideNavItem("Home ", HomeView.class, LineAwesomeIcon.HOME_SOLID.create());
        SideNavItem dashboardItem1=new SideNavItem("General ", DashboardView.class, LineAwesomeIcon.ADDRESS_BOOK.create());
        SideNavItem dashboardItem2=new SideNavItem(" Yearly Data", YearDashboardView.class, LineAwesomeIcon.CALENDAR.create());
        SideNavItem dashboardItem3=new SideNavItem(" Statistics ", StatsView.class, LineAwesomeIcon.SYMFONY.create());
        dashboardItems.addItem(dashboardItem,dashboardItem1, dashboardItem2, dashboardItem3);
        nav.addItem(dashboardItems);
        //nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.ACCUSOFT.create()));
        //nav.addItem(new SideNavItem("LGD DATA", lgd.class, LineAwesomeIcon.ACCUSOFT.create()));
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
        SideNavItem preverify=new SideNavItem("Coordinates Verification", PreCrowdView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        SideNavItem verify=new SideNavItem("Verification", CrowdView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        crowd.addItem(preverify, verify);
        nav.addItem(crowd);
        SideNavItem master=new SideNavItem("Master Data", MasterView.class, LineAwesomeIcon.ACCESSIBLE_ICON.create());
        nav.addItem(master);
        SideNavItem lgd=new SideNavItem("LGD Data", lgd.class, LineAwesomeIcon.ZHIHU.create());
        nav.addItem(lgd);
        SideNavItem audit=new SideNavItem("Audit Trail", AuditView.class, LineAwesomeIcon.DOCKER.create());
        nav.addItem(audit);
        
        crowd.setVisible(isVerifiers());
        master.setVisible(isStateAdmin());
        preverify.setVisible(isStateUser());
        verify.setVisible(isVerifierAndSuper());
        category1.setVisible(!isVerifier());
        category2.setVisible(!isVerifier());
        category3.setVisible(!isVerifier());
        village.setVisible(!isVerifier());
        lgd.setVisible(isSuperAdmin());
        audit.setVisible(isSuperAdmin());
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
    	final UserView user=new UserView(dbservice, userservice, securityService);
		user.createAbout();
	}

	public void logout() {
		//auditobject=new Au
		auditobject.saveAudit("Logged Out", userservice.getLoggedUserName());
		securityService.logout();
	}
	public void createUser() {
		final UserView user=new UserView(dbservice, userservice, securityService);
		user.createUser();
	}
	public void changePassword() {
		final UserView user=new UserView(dbservice, userservice, securityService);
		user.changePassword();
	}
}
















