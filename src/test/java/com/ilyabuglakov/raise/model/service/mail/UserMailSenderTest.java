package com.ilyabuglakov.raise.model.service.mail;

import org.testng.annotations.Test;

import javax.mail.MessagingException;

public class UserMailSenderTest {

    @Test
    public void testSendMail() throws MessagingException {
        MailSender mailSender = new UserMailSender();
        mailSender.sendMail("neonlightnight@gmail.com", "Test mail", "Hey, hello");
    }
}