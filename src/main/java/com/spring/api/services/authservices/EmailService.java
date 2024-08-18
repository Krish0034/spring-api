package com.spring.api.services.authservices;

public interface EmailService {
    String sendEmail(String from, String to,String verificationCode);
}
