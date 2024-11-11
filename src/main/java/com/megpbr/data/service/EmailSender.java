package com.megpbr.data.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
public class EmailSender {

    public static void sendEmail(String to, String subject, String body) {
        final String from = "marwein@gmail.com";
        final String password = "pewcybuszxtudsku "; // Use an App Password if using 2FA
        // Gmail SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        //props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.ssl.enable", "true");
        // Get the Session object
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(from));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);

            //System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}