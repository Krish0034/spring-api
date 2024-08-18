package com.spring.api.services.authservices.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import com.spring.api.dto.OtpVerificationDto;
import com.spring.api.dto.UserDto;
import com.spring.api.dto.VerificationTokenDto;
import com.spring.api.dto.VerificationTokenResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.model.auth.User;
import com.spring.api.model.auth.VerificationToken;
import com.spring.api.repository.registration.UserRepository;
import com.spring.api.repository.registration.VerificationTokenRepository;
import com.spring.api.services.authservices.VerificationTokenService;
import com.spring.api.util.EncryptionUtil;
import com.spring.api.util.JwtUtil;
import com.spring.api.util.ResponseMessage;
import com.spring.api.util.StatusUtil;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;
  

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

    @Override
    public VerificationTokenResponse veriyOtp(OtpVerificationDto otpVerificationDto) {
        logger.info("OtpVerificationDto Data: {}", otpVerificationDto.toString());

        if (otpVerificationDto.getToken() != null && otpVerificationDto.getOtp() != null) {

            VerificationTokenDto existVerificationToken = getVerificationToken(otpVerificationDto.getToken());
            // logger.info("Existing VerificationToken Data: {}", existVerificationToken);
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime expiryDatePlusTwoMinutes = existVerificationToken.getExpiryDate().plusMinutes(2);
            System.out.println("Token data is " + currentDate + verificationTokenRepository.isTokenExpiringWithinRange(existVerificationToken.getVerificationId(), currentDate, expiryDatePlusTwoMinutes));
            if (existVerificationToken != null && !verificationTokenRepository.isTokenExpiringWithinRange(existVerificationToken.getVerificationId(), currentDate, expiryDatePlusTwoMinutes)) {
                //
                logger.info("VerificationToken is not expire: {}", existVerificationToken.toString());
                String existingOtp = "";
                System.out.println("otp is 1"+existVerificationToken.getUserId());
                User existingUser = getUserDetialsForUpdate(existVerificationToken.getUserId());
                
                logger.info("VerificationToken existingUser: {}", existingUser.getOtp());
                System.out.println("Exsiting otp is "+ existingUser.getOtp());
                try {
                    existingOtp = EncryptionUtil.decryptText(existingUser.getOtp());
                    logger.info("decryptText existingOtp: {}", existingOtp.toString());
                   
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!existingOtp.isEmpty() && existingOtp.equals(otpVerificationDto.getOtp())) {
                    logger.info("Verify otp: {}", existingOtp.toString());
                    existingUser.setEmailIsVerified(true);
                    existingUser.setOtp(null);
                    return 
                    // new VerificationTokenResponse();
                    saveVerificationToken(userRepository.save(existingUser));
                }
            } else {
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.TOKEN_EXPIRED_OR_INVALID);
            }
        }
        // Handle username and phone cases
        else {
            if (otpVerificationDto.getToken() == null) {
                throw new CustomException(StatusUtil.NOT_FOUND, ResponseMessage.TOKEN_FIELD_EMPTY);
            }
            if (otpVerificationDto.getOtp() == null) {
                throw new CustomException(StatusUtil.NOT_FOUND, ResponseMessage.OTP_FIELD_EMPTY);
            }
        }
        return null;
    }

    public VerificationTokenDto getVerificationToken(String token) {
        return verificationTokenRepository.findByTokenByToken(token);
    }
    @Override
    public VerificationToken getVerificationTokenByToken(String token) {
        if (token == null) {
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.TOKEN_FIELD_EMPTY);
        }
        try {
            VerificationToken existingVerificationTokenDto = 
            verificationTokenRepository.findByToken(token);

            if (existingVerificationTokenDto == null) {
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.TOKEN_NOT_FOUND);
            }
            System.out.println("Get verificatioin : " + existingVerificationTokenDto);
            VerificationToken responseToken=new VerificationToken();
            responseToken.setVerificationId(existingVerificationTokenDto.getVerificationId());
            // responseToken.setUser(existingVerificationTokenDto.getUserId());
            responseToken.setToken(existingVerificationTokenDto.getToken());
            responseToken.setExpiryDate(existingVerificationTokenDto.getExpiryDate());
            return responseToken;
        } catch (Exception e) {
            logger.error("getVerificationTokenByToken : " + e.getMessage(), e);
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
        }
    }

    @Override
    public VerificationTokenResponse saveVerificationToken(User user) {
        VerificationTokenResponse verificationTokenResponse = new VerificationTokenResponse();

        // Retrieve the managed user
        User managedUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND));

        // Find existing token for the user
        VerificationTokenDto existingToken = verificationTokenRepository.findTokenByUserId(managedUser.getUserId());
        VerificationToken verificationToken = new VerificationToken();
        String generatedToken = jwtUtil.generateToken(managedUser.getEmail(), 5 * 60 * 1000);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (existingToken != null ) {
            System.out.println("udate token" + existingToken);
            // Update existing token
            verificationToken.setVerificationId(existingToken.getVerificationId());
            verificationToken.setUser(managedUser);
            verificationToken.setToken(generatedToken);
            verificationToken.setExpiryDate(currentDateTime);
            try {
                verificationToken = verificationTokenRepository.save(verificationToken);
                logger.debug("Verification token updated: {}", verificationToken.getToken());
            } catch (Exception e) {
                logger.error("Error updating verification token: {}", e.getMessage());
                throw new CustomException(StatusUtil.BAD_REQUEST, e.getMessage());
            }
        } 
        else {
            
            /// Create a new VerificationToken           
            verificationToken.setUser(managedUser);
            verificationToken.setToken(generatedToken);
            verificationToken.setExpiryDate(currentDateTime);
            System.out.println("new token" + existingToken);
            System.out.println("new token" + existingToken);

            try {
                verificationToken = verificationTokenRepository.save(verificationToken);
                logger.debug("Verification token saved: {}", verificationToken.getToken());
            } catch (Exception e) {
                logger.error("Error saving verification token: {}", e.getMessage());
                throw new CustomException(StatusUtil.BAD_REQUEST, e.getMessage());
            }
        }

        // Create and return the DTO
        verificationTokenResponse.setVerificationId(verificationToken.getVerificationId());
        verificationTokenResponse.setUser(convertDtoToUser(managedUser));
        verificationTokenResponse.setToken(verificationToken.getToken());
        verificationTokenResponse.setExpiryDate(verificationToken.getExpiryDate());

        return verificationTokenResponse;
    }

    public UserDto convertDtoToUser(User userDto) {
        return new UserDto(userDto.getUserId(), userDto.getUsername(), userDto.getEmail(), userDto.getPhone(),
                userDto.getFirstName(), userDto.getLastName(), userDto.getProfilePicUrl(), userDto.getEmailIsVerified(),
                userDto.getPhoneIsVerified(), userDto.getRoles());
    }

    public User getUserDetialsForUpdate(Long userId) {
        if (userId == null) {
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_ID_NOT_NULL);
        }
        try {
            System.out.println("Fetching user with ID: " + userId);
            User user = userRepository.findUserByUserIdNative(userId);
            if (user == null) {
                System.out.println("No user found with ID: " + userId);
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
            }
            return user;
        } catch (Exception e) {
            System.err.println("Error occurred while fetching user: " + e.getMessage());
            e.printStackTrace();
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
        }
    }

}
