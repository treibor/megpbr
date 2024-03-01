package com.megpbr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import jakarta.servlet.ServletContext;

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
@PWA(name = "MegPbr",
shortName = "MegPbr")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext.setSessionTimeout(30); // Timeout in minutes
    }
	
	  @Bean
	  public FilterRegistrationBean<HiddenHttpMethodFilter>
	  hiddenHttpMethodFilterRegistration() {
	  FilterRegistrationBean<HiddenHttpMethodFilter> registrationBean = new
	  FilterRegistrationBean<>( new HiddenHttpMethodFilter());
	  registrationBean.setEnabled(false); return registrationBean; }
	 
}
