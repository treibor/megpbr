package com.megpbr.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.megpbr.views.login.CryptLogin;
import com.megpbr.views.login.CustomLoginView;
import com.megpbr.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	@Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        ConcurrentSessionControlAuthenticationStrategy concurrentStrategy = 
                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurrentStrategy.setMaximumSessions(1);
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }
	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	
	
	  @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration
	  authenticationConfiguration) throws Exception { return
	  authenticationConfiguration.getAuthenticationManager(); }
	 
	/*
	 * @Bean AuthenticationManager authenticationManager( UserDetailsService
	 * userDetailsService, PasswordEncoder passwordEncoder) {
	 * DaoAuthenticationProvider authenticationProvider = new
	 * DaoAuthenticationProvider();
	 * authenticationProvider.setUserDetailsService(userDetailsService);
	 * authenticationProvider.setPasswordEncoder(passwordEncoder);
	 * 
	 * ProviderManager providerManager = new
	 * ProviderManager(authenticationProvider);
	 * providerManager.setEraseCredentialsAfterAuthentication(false);
	 * 
	 * return providerManager; }
	 */
	
	  @Bean SecurityContextRepository securityContextRepository() { return new
	  DelegatingSecurityContextRepository(new
	  RequestAttributeSecurityContextRepository(), new
	  HttpSessionSecurityContextRepository()); }
	 
	/*
	 * @Bean SecurityContextRepository securityContextRepository() { return new
	 * HttpSessionSecurityContextRepository(); }
	 */
	@Bean
	Filter disableOptionsMethodFilter() {
		return new Filter() {
			@Override
			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest request = (HttpServletRequest) req;
				HttpServletResponse response = (HttpServletResponse) res;
				String method = request.getMethod();
				if ("OPTIONS".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)
						|| "PUT".equals(method)) {
					response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				} else {
					chain.doFilter(req, res);
				}
			}
		};
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(disableOptionsMethodFilter(), ChannelProcessingFilter.class)
		
		.headers(headers -> headers
				.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
				.frameOptions(frame -> frame.sameOrigin())
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN)))
				
				.sessionManagement(session -> session
						.requireExplicitAuthenticationStrategy(false)
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.invalidSessionUrl("/").maximumSessions(1).expiredUrl("/").maxSessionsPreventsLogin(false)
						
						//.sessionRegistry(sessionRegistry())
						);
		setLoginView(http, CryptLogin.class);
		//setLoginView(http, CustomLoginView.class);
		//setLoginView(http, LoginView.class);
		super.configure(http);
	}

	
}
