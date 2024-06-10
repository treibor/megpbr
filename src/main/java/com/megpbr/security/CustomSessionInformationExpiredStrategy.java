package com.megpbr.security;

import java.io.IOException;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private String expiredUrl = "";

    public CustomSessionInformationExpiredStrategy(String expiredUrl) {
        this.expiredUrl = expiredUrl;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {

        HttpServletRequest request = sessionInformationExpiredEvent.getRequest();
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        request.getSession();// creates a new session
        response.sendRedirect(request.getContextPath() + expiredUrl);
    }

}