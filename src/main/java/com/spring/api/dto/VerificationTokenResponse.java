package com.spring.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTokenResponse 
{
    private Long verificationId;
    private UserDto user;
    private String token;
    private LocalDateTime expiryDate;
}
