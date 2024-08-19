package com.spring.api.services.authservices.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.api.exception.CustomException;
import com.spring.api.model.auth.User;
import com.spring.api.repository.registration.UserRepository;
import com.spring.api.util.ResponseMessage;
import com.spring.api.util.StatusUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // return emailId -> {
        //     logger.info("Searching for user: {}", emailId);
        //     return userRepository.findByEmail(emailId)
        //         .orElseThrow(() -> new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND));
        // };
        User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(StatusUtil.BAD_REQUEST, ResponseMessage.USER_NOT_FOUND));;
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }
}
