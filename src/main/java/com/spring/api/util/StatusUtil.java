package com.spring.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class StatusUtil {
   public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
   public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
   public static final HttpStatus ACCEPTED = HttpStatus.ACCEPTED;
   public static final HttpStatus BAD_GATEWAY = HttpStatus.BAD_GATEWAY;
   public static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
   public static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;
   public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    
}
