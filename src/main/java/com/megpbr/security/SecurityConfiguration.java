package com.megpbr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.megpbr.views.login.CustomLoginView;
import com.megpbr.views.login.LoginView;
import com.megpbr.views.unauthenticated.AboutView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}
	 
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.DELETE.toString())).denyAll()
				.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString())).denyAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/**")).denyAll()
				
		)

				.headers(headers -> headers

						.addHeaderWriter(new StaticHeadersWriter("Set-Cookie", "SameSite=Strict; HttpOnly; Secure;"))
						.xssProtection(
								xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
						.frameOptions(frame -> frame.sameOrigin())
						.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
				/*
				 * .contentSecurityPolicy(content -> content
				 * .policyDirectives("default-src 'self' ; " +
				 * "img-src 'self'; media-src 'self'; " +
				 * "object-src 'self'; script-src 'unsafe-inline' " + "'unsafe-eval' 'self'; " +
				 * "style-src 'unsafe-inline' 'self'"))
				 */

				).sessionManagement(session -> session

						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/")
						.maximumSessions(1).expiredUrl("/").maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry())

				)
				

		;
		setLoginView(http, LoginView.class);
		//setLoginView(http, CustomLoginView.class);
		super.configure(http);
	}
	

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

}
