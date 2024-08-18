package com.spring.api.services.authservices;

public interface TwilioService 
{
    public String sendOtpOnPhone(String toPhoneNumber,String verificationCode);
}
