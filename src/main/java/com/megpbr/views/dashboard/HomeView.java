package com.megpbr.views.dashboard;

import com.megpbr.data.service.DashboardService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.UserService;
import com.megpbr.views.MainLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {
	
    public HomeView(DashboardService dservice, Dbservice dbservice, UserService uservice) {
    	 //VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());
    	Image img = new Image("images/logo.png", "image");
        //img.setWidth("200px");
        add(img);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
    }
   
   
}
