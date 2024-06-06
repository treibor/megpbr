package com.megpbr.security;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

@Component
public class MethodFilter extends OncePerRequestFilter { 

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException { 
        if (request.getMethod().equals("OPTIONS")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else { 
            filterChain.doFilter(request, response); 
        } 
    }
}