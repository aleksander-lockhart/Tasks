package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(final Mail mail) {

        try {
            SimpleMailMessage mailMessage = createMailMessage(mail);
            javaMailSender.send(mailMessage);
            LOGGER.info("Email has been sent");
        } catch (MailException e) {
            LOGGER.info("Send mail app failed: " + e.getMessage(), e);
        }
    }

    private SimpleMailMessage createMailMessage(final Mail mail) {

        SimpleMailMessage setMail = new SimpleMailMessage();
        if (mail.getCC() != null) {
            setMail.setCc(mail.getCC());
        } else {
            LOGGER.info("CC empty, no CC set");
        }
        setMail.setTo(mail.getMailTo());
        setMail.setSubject(mail.getSubject());
        setMail.setText(mail.getMessage());
        return setMail;

    }
}
