package com.spring.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.api.dto.LoginUserDto;
import com.spring.api.dto.OtpVerificationDto;
import com.spring.api.dto.UserDto;
import com.spring.api.exception.ApiResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.exception.ResponseBuilder;
import com.spring.api.services.authservices.UserService;
import com.spring.api.services.authservices.VerificationTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin-auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;
   
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /// Admin Signup 

    @RequestMapping(value = "/admin-signup", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> adminSignup(@RequestBody @Valid UserDto userDto) {
        try 
        {
            logger.info("Admin Signup api hit.");
            /// 1 for admin user 
            return ResponseBuilder.success(userService.adminSignup(userDto,1), "Registration successful");
        } 
        catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
    @RequestMapping(value = "/user-signup", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> userSignUp(@RequestBody @Valid UserDto userDto) {
        try 
        {
            logger.info("Admin Signup api hit.");
            // 2 for user
            return ResponseBuilder.success(userService.adminSignup(userDto,2), "Registration successful");
        } 
        catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
    @GetMapping("/user-by-id")
    public ResponseEntity<ApiResponse<?>> getUserById(@RequestParam("user_id") Long userId) {
        try {
            return ResponseBuilder.success(userService.getUserByUserId(userId), "Get successful");
        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
   
   
    @PostMapping("/verify_otp")
    public ResponseEntity<ApiResponse<?>> veriyOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        try {
            logger.info("Request boady veriyOtp {}",otpVerificationDto.toString());
            return ResponseBuilder.success(verificationTokenService.veriyOtp(otpVerificationDto), "Get successful");
        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginUserDto loginUserDto) {
        try {
            logger.info("Request boady Login {}",loginUserDto.toString());
            return ResponseBuilder.success(userService.login(loginUserDto), "Login successful");
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Test endpoint working");
    }

    
}
