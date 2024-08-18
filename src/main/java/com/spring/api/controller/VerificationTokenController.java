package com.spring.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.OtpVerificationDto;
import com.spring.api.exception.ApiResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.exception.ResponseBuilder;
import com.spring.api.services.authservices.VerificationTokenService;

@RestController
@RequestMapping("api/auth")
public class VerificationTokenController 
{
    @Autowired
    private VerificationTokenService verificationTokenService;
    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenController.class);
    
    @PostMapping("/verify_otp")
    public ResponseEntity<ApiResponse<?>> veriyOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        try {
            logger.info("Request boady veriyOtp {}",otpVerificationDto.toString());
            return ResponseBuilder.success(verificationTokenService.veriyOtp(otpVerificationDto), "Get successful");
        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<?>> getVerificationTokenByToken(@RequestParam("token") String token) {
        try {
            return ResponseBuilder.success(verificationTokenService.getVerificationTokenByToken(token), "Get successful");
        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
}
