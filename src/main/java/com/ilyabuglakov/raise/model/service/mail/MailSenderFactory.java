package com.ilyabuglakov.raise.model.service.mail;

/**
 * The type Mail sender factory.
 */
public class MailSenderFactory {

    /**
     * Create mail sender.
     *
     * @return the mail sender
     */
    public MailSender createMailSender() {
        return new UserMailSender();
    }

}
