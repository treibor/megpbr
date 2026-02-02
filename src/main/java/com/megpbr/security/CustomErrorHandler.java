package com.megpbr.security;




import java.io.Serializable;

import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.SessionExpiredException;

public class CustomErrorHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent event) {
        // Always log the exception
        Throwable t = event.getThrowable();
        LoggerFactory.getLogger(getClass()).error("Uncaught UI exception", t);

        // Only if there is a UI bound, do client-side logic
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                ui.getPage().executeJs(
                    "alert($0);", 
                    "An unexpected error occurred: " + t.getMessage()
                );
            });
        }
        // Otherwise, just swallow or rethrow/log as needed
    }
}