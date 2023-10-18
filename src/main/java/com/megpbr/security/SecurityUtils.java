package com.megpbr.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;

import jakarta.servlet.ServletException;

public class SecurityUtils {
    private static final String LOGOUT_SUCCESS_URL = "/";
    public static boolean isAuthenticated() {
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        return req != null && req.getUserPrincipal() != null;
    }
    public static boolean authentication(String username, String password) {
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        if (req == null) {
            // This is in a background thread, and we cannot access the request to
            // log in the user
            return false;
        }
        try {
            req.login(username, password);
            return true;
        } catch (ServletException e) {
            // login exception handle code omitted
            return false;
        }
    }
    public static void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
    }
}