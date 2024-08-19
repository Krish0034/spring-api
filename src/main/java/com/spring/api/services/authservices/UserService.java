package com.spring.api.services.authservices;

import com.spring.api.dto.LoginResponse;
import com.spring.api.dto.LoginUserDto;
import com.spring.api.dto.UserDto;
import com.spring.api.dto.VerificationTokenResponse;
import com.spring.api.model.auth.User;
import java.util.List;

public interface UserService 
{
    public VerificationTokenResponse adminSignup(UserDto userDtoRequest,Integer userType);
    public UserDto getUserByUserId(Long userId);
    public User getUserDetialsForUpdate(Long userId); 
    public LoginResponse login(LoginUserDto loginUserDto); 
    public List<UserDto> getAllUsersDetails(); 

}
