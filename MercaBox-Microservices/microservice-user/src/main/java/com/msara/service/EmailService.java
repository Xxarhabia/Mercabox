package com.msara.service;

import com.msara.domain.entity.MailConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private MailConfigService mailConfigService;

    private JavaMailSender getMailSender() {
        MailConfigEntity mailConfig = mailConfigService.getEmailConfig();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailSender.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.isSmtpAuth());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnable());

        return mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        JavaMailSender mailSender = getMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        MailConfigEntity mailConfig = mailConfigService.getEmailConfig();
        message.setFrom(mailConfig.getUsername());
        mailSender.send(message);
    }
}
