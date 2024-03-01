package com.megpbr.error;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionListenerConfig {

    @Bean
    public ServletListenerRegistrationBean<MySessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new MySessionListener());
    }
}