package com.megpbr.security;

import java.util.Arrays;

import org.apache.catalina.SessionListener;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {
//public class SecurityConfiguration extends VaadinWebSecurity {
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests(authorize -> authorize
	            .requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll()
	            .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
	            //.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.POST.toString())).permitAll()
	            .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.DELETE.toString())).denyAll()
	            .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString())).denyAll()
	            
	        )
	        .headers(headers -> headers
	        		
	            .addHeaderWriter(new StaticHeadersWriter("Set-Cookie", "SameSite=Strict; HttpOnly; Secure;"))
	            .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
	            .frameOptions(frame -> frame.sameOrigin())
	            .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
	            .contentSecurityPolicy(content -> content
	                .policyDirectives("default-src 'self' ; " + "img-src 'self'; media-src 'self'; "
	                        + "object-src 'self'; script-src 'unsafe-inline' " + "'unsafe-eval' 'self'; "
	                        + "style-src 'unsafe-inline' 'self'")
	            )
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	            .sessionFixation().none()
	            .invalidSessionUrl("/login")
	            .maximumSessions(1)
	            .expiredUrl("/login")
	        );

	    super.configure(http);
	    // Set your login view if needed
	    setLoginView(http, LoginViewAudit.class);
	}

	/*
	 * @Bean public SessionRegistry sessionRegistry() { return new
	 * SessionRegistryImpl(); }
	 * 
	 * @Bean public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	 * return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
	 * }
	 */

}
