package com.msara.config;

import com.msara.domain.entity.MailConfigEntity;
import com.msara.service.MailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private MailConfigService mailConfigService;

    @Bean
    public JavaMailSender javaMailSender() {
        MailConfigEntity mailConfig = mailConfigService.getEmailConfig();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailConfig.isSmtpAuth());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnable());
        props.put("mail.debug", "true");

        return mailSender;
    }
}
