package com.msara.service;

import com.msara.domain.entity.MailConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private MailConfigService mailConfigService;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String message) {
        MailConfigEntity mailConfig = mailConfigService.getEmailConfig();
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailConfig.getUsername());
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
