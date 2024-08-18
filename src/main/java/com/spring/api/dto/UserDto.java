package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.model.auth.Role;
import com.spring.api.util.ResponseMessage;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Nullable
    private Long userId;

    @Nullable
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Nullable
    private String password;

    @Email(message = ResponseMessage.EMAIL_VALIDATION)
    @Nullable
    private String email;

    @Nullable
    private String phone;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String profilePicUrl;

    private Boolean emailIsVerified = false;
    private Boolean phoneIsVerified = false;

    @Nullable
    private Set<Role> roles = new HashSet<>();

    @Nullable
    private String verificationToken;

    public UserDto(
            Long userId,
            String username,
            String email,
            String phone,
            String firstName,
            String lastName,
            String profilePicUrl,
            Boolean emailIsVerified,
            Boolean phoneIsVerified,
            Set<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicUrl = profilePicUrl;
        this.emailIsVerified = emailIsVerified;
        this.phoneIsVerified = phoneIsVerified;
        this.roles = roles != null ? roles : new HashSet<>();
    }
    public UserDto(
            Long userId,
            String username,
            String email,
            String phone,
            String firstName,
            String lastName,
            String profilePicUrl,
            Boolean emailIsVerified,
            Boolean phoneIsVerified) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicUrl = profilePicUrl;
        this.emailIsVerified = emailIsVerified;
        this.phoneIsVerified = phoneIsVerified;
    }
}
