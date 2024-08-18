package com.spring.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.api.dto.UserDto;
import com.spring.api.exception.ApiResponse;
import com.spring.api.exception.CustomException;
import com.spring.api.exception.ResponseBuilder;
import com.spring.api.services.authservices.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /// Geerting api
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> hello() {
        try {
            return ResponseBuilder.success("exampleData", "Request was successful");

        } catch (CustomException e) {
            return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);

        }
    }

    /// Admin Signup 

    @RequestMapping(value = "/admin-signup", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> adminSignup(@RequestBody @Valid UserDto userDto) {
        try 
        {
            logger.info("Admin Signup api hit.");
            return ResponseBuilder.success(userService.adminSignup(userDto), "Registration successful");
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
   
   

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Test endpoint working");
    }

    
}
