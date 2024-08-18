package com.spring.api.services.authservices;

import com.spring.api.dto.OtpVerificationDto;
import com.spring.api.dto.VerificationTokenResponse;
import com.spring.api.model.auth.User;
import com.spring.api.model.auth.VerificationToken;

public interface VerificationTokenService {
    
    public VerificationTokenResponse veriyOtp(OtpVerificationDto otpVerificationDto); 
    public VerificationTokenResponse saveVerificationToken(User user);
    public VerificationToken getVerificationTokenByToken(String token);
} 