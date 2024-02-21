package com.megpbr.error;
import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

// ...

public class MyVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        VaadinService.getCurrent().setSystemMessagesProvider(info -> {
            CustomizedSystemMessages messages = new CustomizedSystemMessages();

            // Disable the session expired notification
            messages.setSessionExpiredNotificationEnabled(false);

            // Set the URL for re-authentication (e.g., a logout page)
            messages.setSessionExpiredURL("/login");

            // Customize internal server error messages
            messages.setInternalErrorCaption("Internal Server Error");
            messages.setInternalErrorMessage("Oops! Something went wrong. Please try again later.");

            // You can explore other properties of CustomizedSystemMessages as needed

            return messages;
        });
    }
}
