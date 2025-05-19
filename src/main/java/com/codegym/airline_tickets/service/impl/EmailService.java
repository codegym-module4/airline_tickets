package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendHtmlEmail(String to, String subject, Booking booking) {
        // Tạo context để Thymeleaf dùng render template
        Context context = new Context();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        context.setVariable("booking", booking);
        context.setVariable("yesterday", yesterday);

        // Render HTML từ template
        String htmlContent = templateEngine.process("user/email/email_review_context", context);

        // Gửi mail
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }
}
