package com.financontrol.service;

import com.financontrol.domain.User;
import java.util.Objects;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendVerificationEmail(User user, String token) {
        String subject = "Verify your account - FinanControl";
        String link = baseUrl + "/verify?token=" + token;
        String content = "<p>Hello " + user.getName() + ",</p>"
                + "<p>Please click the link below to verify your registration:</p>"
                + "<p><a href=\"" + link + "\">Verify my account</a></p>"
                + "<br>"
                + "<p>Ignore this email if you did not remember registering.</p>";

        sendHtmlEmail(user.getEmail(), subject, content);
    }

    @Async
    public void sendPasswordRecoveryEmail(User user, String token) {
        String subject = "Password Reset Request - FinanControl";
        String link = baseUrl + "/reset-password?token=" + token;
        String content = "<p>Hello " + user.getName() + ",</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do not want to change your password.</p>";

        sendHtmlEmail(user.getEmail(), subject, content);
    }

    private void sendHtmlEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
