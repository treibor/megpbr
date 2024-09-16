package com.megpbr.security;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private static final int REQUEST_THRESHOLD = 20;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	//System.out.println(request.getRequestURI());
        if ("POST".equalsIgnoreCase(request.getMethod()) &&
            request.getQueryString() != null &&
            request.getQueryString().contains("v-r=uidl") &&
        	"/".equals(request.getRequestURI()))
             {
        	System.out.println(request.getRequestURI());
        	System.out.println("Login Attempt");
            String clientIP = request.getRemoteAddr();
            Bucket bucket = ipBuckets.computeIfAbsent(clientIP, this::createBucket);

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
                System.out.println("Rate limit exceeded for IP: " + clientIP);
                return;
            }
        } else {
        	//System.out.println("No Loin Attem");
            filterChain.doFilter(request, response);
        }
    }

    private Bucket createBucket(String key) {
        Refill refill = Refill.intervally(REQUEST_THRESHOLD, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(REQUEST_THRESHOLD, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    
}