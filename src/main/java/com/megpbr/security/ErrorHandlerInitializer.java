package com.megpbr.security;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;

@Component
public class ErrorHandlerInitializer {

    @EventListener
    public void onApplicationEvent(org.springframework.context.event.ContextRefreshedEvent event) {
        // Wait until the Vaadin session is initialized
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setErrorHandler(new CustomErrorHandler());
        }
    }
}
