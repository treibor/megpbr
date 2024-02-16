package com.megpbr.security;

import java.io.IOException;
import java.net.URLDecoder;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnceFilterJbs extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       // Force Redirection
    	if (request.getScheme().equals("http") && !"localhost".equals(request.getServerName()) && !"127.0.0.1".equals(request.getServerName()) 
               && !"10.179.2.106".equals(request.getServerName()) // Staging Server
               && !"10.179.2.199".equals(request.getServerName()) // Local Server
               & !"0:0:0:0:0:0:0:1".equals(request.getServerName())) {
            String url = "https://" + request.getServerName() + request.getRequestURI();
            response.sendRedirect(url);
        } else {
            boolean forbidden= false;
            if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendRedirect("error?q=400");
                return;
            }
            if (request.getQueryString() != null) {
                String queryParams = request.getQueryString();
                try {
                if(queryParams.contains("%") || queryParams.contains("<") || queryParams.contains(">") || queryParams.contains("'") || queryParams.contains("..") || queryParams.contains("../")){
                      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                      response.sendRedirect("error?q=400");
                      return;
                  }
                  URLDecoder.decode(queryParams, "UTF-8");
                }
                catch(Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect("error?q=400");
                    return;
                }
            }
            if (forbidden) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendRedirect("error.jsp?q=403");
                return;
            } else {
                filterChain.doFilter(request, response);
            }
            
        }
    }
}