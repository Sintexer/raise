package com.ilyabuglakov.raise.model.service.mail;

import javax.mail.MessagingException;

/**
 * The interface Mail sender.
 */
public interface MailSender {
    /**
     * Send mail to specified email.
     *
     * @param email       the email
     * @param title       the title
     * @param messageBody the message body
     * @throws MessagingException the messaging exception
     */
    void sendMail(String email, String title, String messageBody) throws MessagingException;
}
