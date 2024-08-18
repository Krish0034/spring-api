package com.spring.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleApiException(CustomException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    // Handle other exceptions here...
}
