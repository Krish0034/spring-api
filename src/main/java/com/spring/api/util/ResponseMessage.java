package com.spring.api.util;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {

    public static final String PASSWORD_EMPTY = "Password cannot be empty.";
    public static final String USERNAME_EXIST = "Username already exists.";
    public static final String EMAIL_EXIST = "Email already exists.";
    public static final String PHONE_EXIST = "Phone number already exists.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String INVALID_REQUEST = "Invalid request.";
    public static final String ROLE_NOT_FOUND = "Role not found.";
    public static final String INVALID_TOKEN = "Invalid JWT token";
    public static final String EXPIRE_TOKEN = "JWT token is expired";
    public static final String UNSUPPORTE_TOKEN = "JWT token is unsupported";
    public static final String EMPTY_TOKEN = "JWT token is empty";
    public static final String EMAIL_VALIDATION = "Invalid email address";
    public static final String USER_ID_NOT_NULL = "User Id not empty.";
    public static final String TOKEN_FIELD_EMPTY = "Token field empty.";
    public static final String TOKEN_NOT_FOUND = "Token not found.";
    public static final String TOKEN_EXPIRED_OR_INVALID = "Token is expired or invalid.";
    public static final String OTP_FIELD_EMPTY = "OTP field empty.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String ACCOUNT_INACTIVE = "Account Inactive.";
    public static final String LOGIN_FAILED = "Login failed.";
    public static final String FETCH_FAILED = "Fetch data failed.";
}
