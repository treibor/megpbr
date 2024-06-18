package com.megpbr.views.dashboard;

import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.service.DashboardService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.UserService;
import com.megpbr.views.MainLayout;
import com.megpbr.views.villages.VillageView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
	
	DashboardService dservice;
	
	public HomeView(DashboardService dservice,Dbservice dbservice, UserService uservice) {
		this.dservice=dservice;
		Image img = new Image("images/logo.png", "image");
		// img.setWidth("200px");
		add(getCards());
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		getStyle().set("text-align", "center");

	}

	private Component getCards() {
		VerticalLayout card1 = createCard(1);
		VerticalLayout card2 = createCard(2);
		VerticalLayout card3 = createCard(3);
		VerticalLayout card4 = createCard(4);
		card4.addClickListener(e -> {
           UI.getCurrent().navigate(VillageView.class);
        });
		return new HorizontalLayout(card1, card2, card3, card4);
	}

	private VerticalLayout createCard(int type) {
		VerticalLayout card = new VerticalLayout();
		card.addClassName("card");

		Icon icon;
        String title;
        String description;


		// Define content based on card type
		switch (type) {
		case 1:
			 icon = VaadinIcon.CHECK.create();
			long title1=dservice.getAllCountByMaster(true);
			title = ""+title1;
			description = "Master PBR Entered";
			break;
		case 2:
			 icon = VaadinIcon.ARCHIVE.create();
			long title2=dservice.getAllCountByMaster(false);
			title = ""+title2;
			description = "Village PBR Entered";
			break;
		case 3:
			 icon = VaadinIcon.STOPWATCH.create();
			long title3=dservice.getCrowdCount();
			title = ""+title3;
			description = "Crowd Sourcing Data";
			break;
		case 4:
			 icon = VaadinIcon.PICTURE.create();
			long title4=dservice.getVillageDetailsCount();
			title = ""+title4;
			description = "Village Details Entered";
			break;
		default:
			 icon = VaadinIcon.BARCODE.create();
			title = "Default Card Title";
			description = "This is a default card description.";
			break;
		}
		

		// Create image component
		 icon.addClassName("card-icon");

		// Create title component
		Span titleLabel = new Span(title);
		titleLabel.addClassName("card-title");

		// Create description component
		Span descriptionLabel = new Span(description);
		descriptionLabel.addClassName("card-description");

		// Create a button
		Button actionButton = new Button("Learn More");
		actionButton.addClassName("card-button");

		// Add components to the card layout
		card.add(icon, titleLabel, descriptionLabel);

		return card;
	}

}
