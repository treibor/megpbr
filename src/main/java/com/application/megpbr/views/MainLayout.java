package com.application.megpbr.views;

import com.application.megpbr.views.agrobiodiversity.AgroBiodiversityView;
import com.application.megpbr.views.cardlist2.CardList2View;
import com.application.megpbr.views.checkoutform.CheckoutFormView;
import com.application.megpbr.views.collaborativemasterdetail2.CollaborativeMasterDetail2View;
import com.application.megpbr.views.dashboard.DashboardView;
import com.application.megpbr.views.domesticateddiversity.DomesticatedDiversityView;
import com.application.megpbr.views.gridwithfilters.GridwithFiltersView;
import com.application.megpbr.views.personform.PersonFormView;
import com.application.megpbr.views.wilddiversity.WildDiversityView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2("MEG-PBR");
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("MegPbr");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        SideNavItem agro=new SideNavItem("AgroBiodiversity");
        agro.setPrefixComponent(LineAwesomeIcon.COLUMNS_SOLID.create());
        SideNavItem subagro1=new SideNavItem("Crop Plants", CropPlantsView.class,
                LineAwesomeIcon.ACCESSIBLE_ICON.create());
		
		  SideNavItem subagro2=new SideNavItem("Fruit Trees", FruitTreesView.class,
		  LineAwesomeIcon.ACCESSIBLE_ICON.create());
		 
        
        agro.addItem(subagro1, subagro2);
        nav.addItem(agro);
        /*nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.FILE.create()));
        nav.addItem(new SideNavItem("Agro Biodiversity", AgroBiodiversityView.class,
                LineAwesomeIcon.COLUMNS_SOLID.create()));
        
        nav.addItem(new SideNavItem("Domesticated Diversity", DomesticatedDiversityView.class,
                LineAwesomeIcon.COLUMNS_SOLID.create()));
        nav.addItem(new SideNavItem("Wild Diversity", WildDiversityView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
        nav.addItem(new SideNavItem("Person Form", PersonFormView.class, LineAwesomeIcon.USER.create()));
        nav.addItem(
                new SideNavItem("Grid with Filters", GridwithFiltersView.class, LineAwesomeIcon.FILTER_SOLID.create()));
        nav.addItem(new SideNavItem("Checkout Form", CheckoutFormView.class, LineAwesomeIcon.CREDIT_CARD.create()));
        nav.addItem(new SideNavItem("Card List2", CardList2View.class, LineAwesomeIcon.LIST_SOLID.create()));
        nav.addItem(new SideNavItem("Collaborative Master-Detail2", CollaborativeMasterDetail2View.class,
                LineAwesomeIcon.COLUMNS_SOLID.create()));
		*/
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
