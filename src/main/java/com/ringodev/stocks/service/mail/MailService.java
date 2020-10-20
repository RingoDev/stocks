package com.ringodev.stocks.service.mail;

import com.ringodev.stocks.service.auth.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class MailService {

    Environment env;
    Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    public MailService(Environment environment) {
        this.env = environment;
    }

    @Bean
    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setUsername(env.containsProperty("MAIL_ADDR") ? env.getProperty("MAIL_ADDR") : "");
        mailSender.setPassword(env.containsProperty("MAIL_PASS") ? env.getProperty("MAIL_PASS") : "");

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.smtp.host", "smtp.world4you.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        mailSender.setJavaMailProperties(props);

        logger.info("Mailsender Password: "+ env.getProperty("MAIL_PASS"));
        logger.info("Mailsender Address: "+ env.getProperty("MAIL_ADDR"));

        return mailSender;
    }


    public void sendSimpleMessage(
            String to, String subject, String text) {
        JavaMailSenderImpl emailSender = getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ringodev.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendVerificationMessage(
            String to)  {

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(to)
                .setExpiration(new Date(System.currentTimeMillis() + ( 1000 * 60 *10)))
                .compact();

        JavaMailSenderImpl emailSender = getJavaMailSender();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
        try {
            helper.setText("Click the "+
                    "<a href=\"https://ringodev.xyz/api/verify?token="+token+"\">link</a>"+ " to verify:\n",true);
            helper.setFrom("noreply@ringodev.com");
            helper.setTo(to);
            helper.setSubject("Verify your RingoDev account");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }
}
