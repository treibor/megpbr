package com.megpbr.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
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
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	/*
	 * @Bean AuthenticationManager authenticationManager(UserDetailsServiceImpl
	 * userDetailsService, PasswordEncoder passwordEncoder) {
	 * DaoAuthenticationProvider authenticationProvider = new
	 * DaoAuthenticationProvider();
	 * authenticationProvider.setUserDetailsService(userDetailsService);
	 * authenticationProvider.setPasswordEncoder(passwordEncoder); return new
	 * ProviderManager(authenticationProvider); }
	 */

	/*
	 * @Bean SecurityContextRepository securityContextRepository() { return new
	 * DelegatingSecurityContextRepository(new
	 * RequestAttributeSecurityContextRepository(), new
	 * HttpSessionSecurityContextRepository()); }
	 */
	@Bean
	SecurityContextRepository securityContextRepository() {
	    return new HttpSessionSecurityContextRepository();
	}
	@Bean
	public Filter disableOptionsMethodFilter() {
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
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.invalidSessionUrl("/").maximumSessions(1).expiredUrl("/").maxSessionsPreventsLogin(false)
						.sessionRegistry(sessionRegistry()));
		setLoginView(http, CryptLogin.class);
		//setLoginView(http, CustomLoginView.class);
		//setLoginView(http, LoginView.class);
		super.configure(http);
	}

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * 
	 * http.authorizeHttpRequests(authorize -> authorize
	 * .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,
	 * "/images/*.png")).permitAll() .requestMatchers(new
	 * AntPathRequestMatcher("/**", HttpMethod.DELETE.toString())).denyAll()
	 * .requestMatchers(new AntPathRequestMatcher("/**",
	 * HttpMethod.OPTIONS.toString())).denyAll()
	 * .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS,
	 * "/**")).denyAll()
	 * 
	 * ).headers(headers -> headers .xssProtection(xss ->
	 * xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
	 * .frameOptions(frame -> frame.sameOrigin()) .referrerPolicy(referrer ->
	 * referrer.policy(ReferrerPolicy.SAME_ORIGIN))
	 * 
	 * ).sessionManagement(session -> session
	 * 
	 * .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl(
	 * "/").maximumSessions(1)
	 * .expiredUrl("/").maxSessionsPreventsLogin(false).sessionRegistry(
	 * sessionRegistry())
	 * 
	 * )
	 * 
	 * ; // setLoginView(http, LoginView.class); setLoginView(http,
	 * CustomLoginView.class); // setLoginView(http,
	 * CustomLoginViewNoCaptcha.class); super.configure(http); }
	 */
	
}
