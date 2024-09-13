package com.megpbr.security;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bucket4j;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AjaxRateLimitingFilter implements Filter {

    // Map to store buckets for each IP address
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    
    // Configuration: max 100 AJAX requests per minute
    private static final int AJAX_REQUEST_THRESHOLD = 100;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = getClientIP(httpRequest);

        // Check if this is an AJAX request (for Vaadin, typically /UIDL)
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/UIDL")) {
            Bucket bucket = ipBuckets.computeIfAbsent(clientIP, this::createBucket);

            if (bucket.tryConsume(1)) {
                // Allow the request if tokens are available
                chain.doFilter(request, response);
            } else {
                // Rate limit exceeded, respond with HTTP 429 (Too Many Requests)
                httpResponse.setStatus(429);
                httpResponse.getWriter().write("Too many AJAX requests");
                System.out.println("Rate limit exceeded for IP: " + clientIP);
            }
        } else {
            // Proceed with the request if it's not an AJAX request
            chain.doFilter(request, response);
        }
    }

    // Method to create a new bucket with the rate limit configuration
    private Bucket createBucket(String key) {
        Refill refill = Refill.greedy(AJAX_REQUEST_THRESHOLD, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(AJAX_REQUEST_THRESHOLD, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    // Utility method to retrieve the client's IP address
    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}