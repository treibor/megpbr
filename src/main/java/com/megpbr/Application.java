package com.megpbr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.megpbr.security.CustomErrorHandler;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "megpbr")
@Push
@PWA(name = "MegPbr", shortName = "MegPbr", iconPath = "/icons/logo.png")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void configurePage(AppShellSettings settings) {
		settings.addFavIcon("icon", "icons/logo.png", "192x192");
		settings.addLink("shortcut icon", "icons/logo.png");
		settings.setPageTitle("MegPbr");
	}

	
}
