package com.megpbr.error;

import com.vaadin.flow.server.VaadinSession;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class MySessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        //System.out.println("Session created: " + session.getId());
        // Add your custom logic here
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
       // System.out.println("Session destroyed: " + session.getId());
        //Add your custom logic here
    }
    
}