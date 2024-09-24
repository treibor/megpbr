package com.megpbr.security;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter2 extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, LinkedList<Long>> requestTimestamps = new ConcurrentHashMap<>();
    private static final int REQUEST_THRESHOLD = 30; // Max requests
    private static final int TIME_WINDOW_SECONDS = 60; // Time window in seconds

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	  
        // Check if the request is a POST with the specific query parameter
        if ("POST".equalsIgnoreCase(request.getMethod()) 
                && request.getQueryString() != null 
                && request.getQueryString().contains("v-r=uidl")) {
            //System.out.println("Performing Rate Limiting");
            String clientIP = request.getRemoteAddr();
            long currentTime = Instant.now().getEpochSecond();
            
            // Track the requests for the client IP
            requestTimestamps.putIfAbsent(clientIP, new LinkedList<>());
            LinkedList<Long> timestamps = requestTimestamps.get(clientIP);
            
            // Remove timestamps outside the time window
            while (!timestamps.isEmpty() && (currentTime - timestamps.peekFirst() > TIME_WINDOW_SECONDS)) {
                timestamps.pollFirst();
            }

            // Check the current number of requests in the time window
            if (timestamps.size() >= REQUEST_THRESHOLD) {
                // Rate limit exceeded
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
                response.getWriter().flush();
                return;
            }

            // Add the current timestamp
            timestamps.add(currentTime);
        }

        // Proceed with the filter chain for all requests
        filterChain.doFilter(request, response);
    }
    
}