package com.kreventplanner.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kreventplanner.entity.EventInquiry;

@Service
public class EmailSenderUtil {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailSenderUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendInquiryAcknowledgment(EventInquiry inquiry) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(inquiry.getEmail());
            message.setCc("mangeshdhande98@gmail.com");
            message.setSubject("Event Inquiry Received - KR Event Planner");

            String body = String.format("Dear %s,\n\n" +
                    "Thank you for contacting KR Event Planner!\n\n" +
                    "We have received your inquiry regarding the %s event. " +
                    "Our team will review your requirements and get back to you shortly.\n\n" +
                    "Inquiry Details:\n" +
                    "- Event Type: %s\n" +
                    "- Event Date: %s\n" +
                    "- Details provided: %s\n\n" +
                    "Best Regards,\n" +
                    "KR Event Planner Team",
                    inquiry.getName(),
                    inquiry.getEventType(),
                    inquiry.getEventType(),
                    inquiry.getEventDate() != null ? inquiry.getEventDate().toString() : "TBD",
                    inquiry.getEventDetails());

            message.setText(body);
            mailSender.send(message);
            System.out.println("Email successfully sent to: " + inquiry.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email to " + inquiry.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
