package com.spring.api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<?>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(
            "success", 
            HttpStatus.OK.value(), 
            message, 
            data
        ));
    }

    public static <T> ResponseEntity<ApiResponse<?>> error(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(
            "Error", 
            status.value(), 
            message, 
            data
        ));
    }
}
