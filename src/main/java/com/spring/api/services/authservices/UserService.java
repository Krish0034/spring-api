package com.spring.api.services.authservices;

import com.spring.api.dto.UserDto;
import com.spring.api.dto.VerificationTokenResponse;
import com.spring.api.model.auth.User;

public interface UserService 
{
    public VerificationTokenResponse adminSignup(UserDto userDtoRequest);
    public UserDto getUserByUserId(Long userId);
    public User getUserDetialsForUpdate(Long userId); 

}
