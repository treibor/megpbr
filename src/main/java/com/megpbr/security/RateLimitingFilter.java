package com.megpbr.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private static final int MAIN_REQUEST_THRESHOLD = 500; // Adjust as needed
    private static final int OTHER_REQUEST_THRESHOLD = 500; // Adjust as needed

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = getClientIP(httpRequest);
        String requestURI = httpRequest.getRequestURI();
        String normalizedURI = normalizeURI(requestURI);

        // Determine if the request is a resource request
        boolean isResourceRequest = isResourceRequest(normalizedURI);

        // Determine bucket based on request type
        Bucket bucket;
        if (isResourceRequest) {
            // Skip rate limiting for resource requests
            //System.out.println("Resource request: " + requestURI + " - not rate-limited.");
            chain.doFilter(request, response);
            return;
        } else if ("/login".equals(normalizedURI) ) {
            bucket = ipBuckets.computeIfAbsent(clientIP, this::createMainRequestBucket);
            //System.out.println("Rate limiting applied to /login  for IP " + clientIP);
        } else {
            bucket = ipBuckets.computeIfAbsent(clientIP, this::createOtherRequestBucket);
            //System.out.println("Rate limiting applied to other paths for IP " + clientIP);
        }

        //long availableTokens = bucket.getAvailableTokens();
        //System.out.println("Available tokens for " + clientIP + ": " + availableTokens);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.getWriter().write("Too many requests");
            //Notification.show("Too Many Requests");
            //System.out.println("Rate limit exceeded for IP " + clientIP);
        }
    }

    private boolean isResourceRequest(String uri) {
        return uri.startsWith("/VAADIN/") || uri.startsWith("/resources/") || uri.endsWith("/error")|| uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".svg");
    }

    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String normalizeURI(String uri) {
        return uri.startsWith("/") ? uri : "/" + uri;
    }

    private Bucket createMainRequestBucket(String key) {
        Refill refill = Refill.greedy(MAIN_REQUEST_THRESHOLD, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(MAIN_REQUEST_THRESHOLD, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    private Bucket createOtherRequestBucket(String key) {
        Refill refill = Refill.greedy(OTHER_REQUEST_THRESHOLD, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(OTHER_REQUEST_THRESHOLD, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void destroy() {
    }
}