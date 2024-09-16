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

    // Map to store buckets for each IP address
    private final ConcurrentHashMap<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    
    // Configuration: max 10 AJAX requests per minute
    private static final int REQUEST_THRESHOLD = 10;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        // Check if the request method is POST and contains 'v-r=uidl' in the query string
        if ("POST".equalsIgnoreCase(request.getMethod()) &&
            request.getQueryString() != null &&
            request.getQueryString().contains("v-r=uidl")) {
        	System.out.println(isLoginAttempt(request));
            String clientIP = request.getRemoteAddr();
            Bucket bucket = ipBuckets.computeIfAbsent(clientIP, this::createBucket);

            if (bucket.tryConsume(1)) {
                // Allow the request if tokens are available
                filterChain.doFilter(request, response);
            } else {
                // Rate limit exceeded, respond with HTTP 429 (Too Many Requests)
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
                System.out.println("Rate limit exceeded for IP: " + clientIP);
                return;
            }
        } else {
            // Proceed with the request if it's not an AJAX request or not a POST request with 'v-r=uidl'
            filterChain.doFilter(request, response);
        }
    }

    // Method to create a new bucket with the rate limit configuration
    private Bucket createBucket(String key) {
        Refill refill = Refill.intervally(REQUEST_THRESHOLD, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(REQUEST_THRESHOLD, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
    private boolean isLoginAttempt(HttpServletRequest request) throws IOException {
        // Read and parse the request body
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Check if login data is present in the request body
        String body = requestBody.toString();
        return body.contains("username") && body.contains("password");
    }
}