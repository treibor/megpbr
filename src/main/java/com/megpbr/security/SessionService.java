package com.megpbr.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vaadin.flow.server.VaadinServletRequest;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

    @Autowired
    private SessionRegistry sessionRegistry;


    public void expireUserSessions(String username) {
    	
        List<Object> users = sessionRegistry.getAllPrincipals();
        System.out.println("USers"+users);
        for (Object principal : users) {
        	System.out.println("A");
                UserDetails user = (UserDetails) principal;
                if (user.getUsername().equals(username)) {
                	System.out.println("A");
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    System.out.println("X");
                    for (SessionInformation sessionInfo : sessions) {
                        sessionInfo.expireNow();
                        // Retrieve the actual HTTP session and invalidate it
                        HttpSession httpSession = sessionInfo.getSessionId() != null ?
                            VaadinServletRequest.getCurrent().getHttpServletRequest().getSession(false) : null;
                        if (httpSession != null && sessionInfo.getSessionId().equals(httpSession.getId())) {
                            httpSession.invalidate(); // Invalidate the HTTP session
                        }
                    }
                }
           
        }
    }
}