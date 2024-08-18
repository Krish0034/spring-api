package com.spring.api.services.authservices.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.api.config.TwilioConfig;
import com.spring.api.services.authservices.TwilioService;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioServiceImpl implements TwilioService {
    @Autowired
    private TwilioConfig twilioConfig;

    @Override
    public String sendOtpOnPhone(String toPhoneNumber, String verificationCode) {

        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioConfig.getTrialNumber()),
                    "Your verification code is: " + verificationCode)
                    .create();

            System.out.println("Send otp on phone is: " + message.getBody());
            return message.getBody();
        } catch (ApiException e) {

            System.err.println("Error sending SMS: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP due to regional restrictions.");
        }

    }

}
