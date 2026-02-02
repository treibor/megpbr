package com.megpbr;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.communication.IndexHtmlResponse;
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

    /**
     * Injects a per-request nonce into the index.html response
     * and sets a strict CSP header using that nonce.
     */
    
    /* Nonce based CSP Directive. Disable PWA and Push
    @Bean
    public VaadinServiceInitListener cspNonceInjector() {
        return initEvent -> initEvent.addIndexHtmlRequestListener(Application::injectCspNonce);
    }

    private static void injectCspNonce(IndexHtmlResponse response) {
        if (!response.getVaadinRequest()
                     .getService()
                     .getDeploymentConfiguration()
                     .isProductionMode()) {
            return;
        }

        String nonce = UUID.randomUUID().toString();

        response.getVaadinResponse().setHeader("Content-Security-Policy",
        	    "default-src 'self'; " +
        	    "script-src 'self' 'nonce-" + nonce + "' 'unsafe-eval'; " +
        	    "style-src 'self' 'unsafe-inline'; " +
        	    //"style-src 'self' 'nonce-" + nonce + "'; " +
        	    "img-src 'self' data:; " +
        	    "font-src 'self' data:; " +
        	    "connect-src 'self'; " +
        	    "object-src 'none'; " +
        	    "frame-ancestors 'self'; " +
        	    "base-uri 'self';"
        	);

        response.getDocument()
                .getElementsByTag("script")
                .forEach(script -> script.attr("nonce", nonce));
    }
    */
}
