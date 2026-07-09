package com.buxbuddy.auth.service;


import com.buxbuddy.auth.dto.login.RegisterRequest;
import com.buxbuddy.auth.dto.login.RegisterResponse;
import com.buxbuddy.auth.dto.user.UpdateUserRequest;
import com.buxbuddy.auth.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    RegisterResponse register(RegisterRequest request);
    RegisterResponse login(RegisterRequest request);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(UpdateUserRequest request);
    void deleteUser();

}