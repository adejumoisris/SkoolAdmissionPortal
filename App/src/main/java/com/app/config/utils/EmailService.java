package com.app.config.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LogManager.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("abdullahi.adedayo@gmail.com");

            log.info("Sending email to {}", to);
            mailSender.send(message);

            log.info("Plain Email sent to {}", to);
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
        }
    }

    @Async
    public void sendEmailWithHtml(String to, String subject, String body) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("no-reply@skool.com");

            log.info("Email with HTML sent to {}", to);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("An error occurred while sending the email: {}", e.getMessage());
        }

    }
}
