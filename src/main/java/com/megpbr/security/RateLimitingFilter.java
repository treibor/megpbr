package com.megpbr.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

	private final ConcurrentHashMap<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
	private static final int REQUEST_THRESHOLD = 30;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if ("POST".equalsIgnoreCase(request.getMethod()) && request.getQueryString() != null
				&& request.getQueryString().contains("v-r=uidl")) {

			String clientIP = request.getRemoteAddr();
			Bucket bucket = ipBuckets.computeIfAbsent(clientIP, this::createBucket);

			if (bucket.tryConsume(1)) {
				filterChain.doFilter(request, response);
			} else {
				response.setStatus(429);
				response.getWriter().write("Too many requests. Please try again later.");
				// System.out.println("Rate limit exceeded for IP: " + clientIP);
				response.getWriter().flush();
				return;
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private Bucket createBucket(String key) {
		Refill refill = Refill.intervally(REQUEST_THRESHOLD, Duration.ofMinutes(2));
		Bandwidth limit = Bandwidth.classic(REQUEST_THRESHOLD, refill);
		return Bucket4j.builder().addLimit(limit).build();
	}
	private boolean isValidUserAgent(String userAgent) {
	    // Check for common browser user agents (Chrome, Firefox, Safari, etc.)
	    // Customize this list based on your needs
	    return userAgent.contains("Chrome") || userAgent.contains("Firefox") || userAgent.contains("Safari");
	}
    
}