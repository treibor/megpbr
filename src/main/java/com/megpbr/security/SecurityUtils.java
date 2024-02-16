package com.megpbr.security;

import java.security.MessageDigest;

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
    
    public static String sha256(String pass) {
        byte buf[] = pass.getBytes();
        byte[] digest = null;
        String hexStr = "";
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
            algorithm.reset();
            algorithm.update(buf);
            digest = algorithm.digest();
            for (int i = 0; i < digest.length; i++) {
                hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Exception ex) {
            return "";
        } finally {
            algorithm = null;
            digest = null;
            buf = null;
        }
        return hexStr;
    }
}