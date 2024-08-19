package com.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.exception.ApiResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.exception.ResponseBuilder;
import com.spring.api.services.authservices.UserService;
@RestController
@RequestMapping("api/user")
public class UserController 
{
    @Autowired
    private UserService userService;
    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<?>> getUserById() {
        try {
            return ResponseBuilder.success(userService.getAllUsersDetails(), "User details fetched successfully");
        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
        }
    }
}
