package com.spring.api.util;
import java.util.Random;

public class UtilFunction
{
    static public String generateOTP(){
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
    
    
}
