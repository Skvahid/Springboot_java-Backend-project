package com.ums.service;
import com.ums.payload.LoginDto;
import com.ums.payload.UserDto;

public interface UserService {

    public UserDto  addUser(UserDto userDto);


    String verifyLogin(LoginDto loginDto);
}
