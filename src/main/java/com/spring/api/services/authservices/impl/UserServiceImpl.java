package com.spring.api.services.authservices.impl;

import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.spring.api.dto.UserDto;
import com.spring.api.dto.VerificationTokenResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.model.auth.Role;
import com.spring.api.model.auth.User;
import com.spring.api.repository.registration.RoleRepository;
import com.spring.api.repository.registration.UserRepository;
import com.spring.api.services.authservices.EmailService;
import com.spring.api.services.authservices.UserService;
import com.spring.api.services.authservices.VerificationTokenService;
import com.spring.api.util.ERole;
import com.spring.api.util.EncryptionUtil;
import com.spring.api.util.ResponseMessage;
import com.spring.api.util.StatusUtil;
import com.spring.api.util.UtilFunction;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VerificationTokenService verificationTokenService;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public VerificationTokenResponse adminSignup(UserDto userDtoRequest) {
        logger.debug("Admin signup request received for email: {}", userDtoRequest.getEmail());
        if (userDtoRequest.getEmail() != null && !userDtoRequest.getEmail().isEmpty()) {
            if (userDtoRequest.getPassword() == null || userDtoRequest.getPassword().isEmpty()) {
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.PASSWORD_EMPTY);
            } else {
                return handleEmailSignup(userDtoRequest);
            }
        }
        // Handle username and phone cases
        if (userDtoRequest.getUsername() != null || userDtoRequest.getPhone() != null) {
            System.out.println("User Details before save : ");
            return updateUserDetails(userDtoRequest);
        } else {
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.INVALID_REQUEST);
        }
    }

    @Override
    public UserDto getUserByUserId(Long userId) {
        if (userId == null) {
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_ID_NOT_NULL);
        }
        try {
            System.out.println("Fetching user with ID: " + userId);
            User user = userRepository.findUserByUserId(userId);

            if (user == null) {
                System.out.println("No user found with ID: " + userId);
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
            }
            UserDto userDTO = new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPhone(),
                    user.getFirstName(), user.getLastName(), user.getProfilePicUrl(), user.getEmailIsVerified(),
                    user.getPhoneIsVerified());
            System.out.println("User found: " + userDTO);
            return userDTO;
        } catch (Exception e) {
            System.err.println("Error occurred while fetching user: " + e.getMessage());
            e.printStackTrace();
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
        }
    }

    @Override
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


    private VerificationTokenResponse handleEmailSignup(UserDto userDtoRequest) {

        if (userRepository.existsByEmail(userDtoRequest.getEmail())) {
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.EMAIL_EXIST);
        } else {
            User user = new User();
            user.setUsername(userDtoRequest.getUsername());
            user.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));
            user.setEmail(userDtoRequest.getEmail());
            user.setPhone(userDtoRequest.getPhone());
            user.setFirstName(userDtoRequest.getFirstName());
            user.setLastName(userDtoRequest.getLastName());
            user.setProfilePicUrl(userDtoRequest.getProfilePicUrl());
            user.setEmailIsVerified(false);
            user.setPhoneIsVerified(false);
            user.setResponseToken(null);
            setAdminRole(user);
            try {
                user.setOtp(EncryptionUtil.encryptText(UtilFunction.generateOTP()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            /// Save User
            user = saveUser(user);
            System.out.println("User Details after save : " + user.toString());
            try {
                sendOtp(user.getEmail(), EncryptionUtil.decryptText(user.getOtp()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            /// Generate verification token
            return verificationTokenService.saveVerificationToken(user);

        }
    }

    private VerificationTokenResponse updateUserDetails(UserDto userDtoRequest) {
        System.out.println("User Details before save : " + userDtoRequest.getUserId());
        // Validate existing user details
        if (userDtoRequest.getUsername() != null) {
            Optional<User> existingUser = userRepository.findByUsername(userDtoRequest.getUsername());

            if (existingUser.isPresent()) {
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USERNAME_EXIST);
            }
        }

        if (userDtoRequest.getPhone() != null) {
            if (userRepository.findByPhone(userDtoRequest.getPhone()) != null) {
                throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.PHONE_EXIST);
            }
        }
        User existingUser = getUserDetialsForUpdate(userDtoRequest.getUserId());
        
        if (existingUser != null) {

            User updateUser = new User();
            updateUser.setUserId(existingUser.getUserId());
            // Update username if present
            updateUser.setUsername(userDtoRequest.getUsername() != null ? userDtoRequest.getUsername() : existingUser.getUsername());
            // Update phone if present
            updateUser.setPhone(userDtoRequest.getPhone() != null ? userDtoRequest.getPhone() : existingUser.getPhone());
            // Update first name if present
            updateUser.setFirstName(userDtoRequest.getFirstName() != null ? userDtoRequest.getFirstName() : existingUser.getFirstName());
            // Update last name if present
            updateUser.setLastName(userDtoRequest.getLastName() != null ? userDtoRequest.getLastName() : existingUser.getLastName());
            // Update profile pic URL if present
            updateUser.setProfilePicUrl(userDtoRequest.getProfilePicUrl() != null ? userDtoRequest.getProfilePicUrl(): existingUser.getProfilePicUrl());
            updateUser.setEmail(userDtoRequest.getEmail() != null ? userDtoRequest.getEmail() : existingUser.getEmail());
            if (userDtoRequest.getPhone() != null) {
                try {
                    updateUser.setOtp(EncryptionUtil.encryptText(UtilFunction.generateOTP()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Preserve the existing fields that should not be updated
            updateUser.setPassword(existingUser.getPassword());
            updateUser.setRoles(existingUser.getRoles());
            updateUser.setEmailIsVerified(existingUser.getEmailIsVerified());
            updateUser.setPhoneIsVerified(existingUser.getPhoneIsVerified());
            System.out.println("User Details before save updated: " + updateUser);
            // Save the updated user
            updateUser = userRepository.save(updateUser);

            // Return the updated verification token
            return verificationTokenService.saveVerificationToken(updateUser);
        } else {
            // Handle case where the user is not found
            throw new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND);
        }

    }

    private void setAdminRole(User user) {
        Role adminRole = roleRepository.findByRoleName(ERole.ADMIN);
        user.setRoles(Collections.singleton(adminRole));
    }


    private String sendOtp(String to, String verificationCode) {
        System.out.println("Send otp by email" + verificationCode);
        return emailService.sendEmail("springboot0034@gmail.com", to, verificationCode);
    }

    private User saveUser(User user) {
        User saveUser;
        try {
            saveUser = userRepository.save(user);
            logger.debug("User saved with ID: {}", user.getUserId());

        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage());
            throw e;
        }
        return saveUser;
    }



}