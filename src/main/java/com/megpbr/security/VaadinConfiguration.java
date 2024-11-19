package com.megpbr.security;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.SystemMessagesProvider;


@Configuration
public class VaadinConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
    @Bean
    SystemMessagesProvider systemMessagesProvider() {
        return (SystemMessagesInfo systemMessagesInfo) -> {
            CustomizedSystemMessages messages = new CustomizedSystemMessages();
            // Suppress default notifications
            messages.setSessionExpiredNotificationEnabled(false);
            messages.setInternalErrorNotificationEnabled(false);
            //messages.setCommunicationErrorNotificationEnabled(false);
            return messages;
        };
    }
}