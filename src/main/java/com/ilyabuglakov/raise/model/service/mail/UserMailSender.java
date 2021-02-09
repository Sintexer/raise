package com.ilyabuglakov.raise.model.service.mail;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The type User mail sender.
 */
public class UserMailSender implements MailSender {

    private final Properties properties;
    private final String serverEmail;
    private final String password;


    /**
     * Instantiates a new User mail sender.
     */
    public UserMailSender() {
        properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        serverEmail = "ilboogl@gmail.com";
        password = "qgohxlvmzttydhlq";
    }

    private Message prepareMessage(Session session, String userEmail, String title, String messageBody) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(serverEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        message.setSubject(title);
        message.setText(messageBody);
        return message;
    }

    @Override
    public void sendMail(String userEmail, String title, String messageBody) throws MessagingException {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(serverEmail, password);
            }
        });
        Transport.send(prepareMessage(session, userEmail, title, messageBody));
    }
}
