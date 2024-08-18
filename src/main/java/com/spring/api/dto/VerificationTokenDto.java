package com.spring.api.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTokenDto {
    private Long verificationId;
    private Long userId;
    private String token;
    private LocalDateTime expiryDate;
}
