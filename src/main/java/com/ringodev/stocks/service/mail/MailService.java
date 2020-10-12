package com.ringodev.stocks.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    Environment env;

    @Autowired
    public MailService(Environment environment) {
        this.env = environment;
    }

    @Bean
    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(env.containsProperty("MAIL_ADDRESS") ? env.getProperty("MAIL_ADDRESS") : "");
        mailSender.setPassword(env.containsProperty("MAIL_PASSWORD") ? env.getProperty("MAIL_PASSWORD") : "");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    public void sendSimpleMessage(
            String to, String subject, String text) {
        JavaMailSender emailSender = getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ringodev.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


}
