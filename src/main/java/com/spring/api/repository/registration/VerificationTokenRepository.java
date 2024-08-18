package com.spring.api.repository.registration;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.api.dto.VerificationTokenDto;
import com.spring.api.model.auth.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

      VerificationToken findByToken(String token);

      @Query("SELECT new com.spring.api.dto.VerificationTokenDto(v.id, v.user.id, v.token, v.expiryDate) FROM VerificationToken v WHERE v.user.id = :userId")
      VerificationTokenDto findTokenByUserId(@Param("userId") Long userId);

      @Query("SELECT new com.spring.api.dto.VerificationTokenDto(v.id, v.user.id, v.token, v.expiryDate) FROM VerificationToken v WHERE v.token = :token")
      VerificationTokenDto findByTokenByToken(@Param("token") String token);

      @Query("SELECT CASE " +
                  "WHEN :currentTime BETWEEN vt.expiryDate AND :expiryDatePlusTwoMinutes THEN true " +
                  "ELSE false " +
                  "END " +
                  "FROM VerificationToken vt " +
                  "WHERE vt.verificationId = :verificationId")
      Boolean isTokenExpiringWithinRange(@Param("verificationId") Long verificationId,
                  @Param("currentTime") LocalDateTime currentTime,
                  @Param("expiryDatePlusTwoMinutes") LocalDateTime expiryDatePlusTwoMinutes);

}
