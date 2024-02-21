package com.megpbr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	@Autowired
	UserDetailsServiceImpl userdetails;
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests(authorize -> authorize
	    		.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll()
	            //.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.POST.toString())).permitAll()
	            .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.DELETE.toString())).denyAll()
	            .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString())).denyAll()
	            .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/**")).denyAll()
	            
	        )
	    	
	        .headers(headers -> headers
	        		
	            .addHeaderWriter(new StaticHeadersWriter("Set-Cookie", "SameSite=Strict; HttpOnly; Secure;"))
	            .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
	            .frameOptions(frame -> frame.sameOrigin())
	            .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
	            /*.contentSecurityPolicy(content -> content
	                .policyDirectives("default-src 'self' ; " + "img-src 'self'; media-src 'self'; "
	                        + "object-src 'self'; script-src 'unsafe-inline' " + "'unsafe-eval' 'self'; "
	                        + "style-src 'unsafe-inline' 'self'"))*/
	            
	        )
	        .sessionManagement(session -> session
	        	
	            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	            .invalidSessionUrl("/")
	            .maximumSessions(1)
	            .sessionRegistry(sessionRegistry())
	            .expiredUrl("/")
	            .maxSessionsPreventsLogin(false)
	        );

	    super.configure(http);
	    // Set your login view if needed
	    setLoginView(http, LoginViewAudit.class);
	}

	@Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

	

}
