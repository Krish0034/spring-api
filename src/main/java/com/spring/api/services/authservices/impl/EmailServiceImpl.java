package com.spring.api.services.authservices.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.spring.api.services.authservices.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {



    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendEmail(String from, String to,String verificationCode) {
        try {
            String htmlTemplate = getHtmlTemplate(to, verificationCode);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(htmlTemplate, true); // Set to true for HTML content
            helper.setTo(to);
            helper.setSubject("Your Verification Code");
            javaMailSender.send(mimeMessage);
            System.out.println("Email sent successfully!");
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
        return "Email sent successfully!";
    }
    private String getHtmlTemplate(String email, String verificationCode) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Email Template</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f; margin: 0; padding: 0; }" +
                ".email-container { background-color: #ffffff; width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #dddddd; }" +
                ".header { background-color: #097969; color: #ffffff; padding: 10px; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".content h1 { font-size: 24px; color: #333333; }" +
                ".content p { font-size: 16px; color: #666666; line-height: 1.5; }" +
                ".footer { padding: 10px; text-align: center; font-size: 12px; color: #999999; border-top: 1px solid #dddddd; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"header\">" +
                "<h2>Email Verification code.</h2>" +
                "</div>" +
                "<div class=\"content\">" +
                "<h1>Hi, " + email + "!</h1>" +
                "<p>Your verification code is: <strong>" + verificationCode + "</strong></p>" +
                "<p>Enter this code in our [website or app] to activate your account.<br>" +
                "If you have any questions, send us an email <b>springboot0034@gmail.com</b>.</p>" +
                "We’re glad you’re here!<br>" +
                "The Krish team" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>© 2024 Your Company Name. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
