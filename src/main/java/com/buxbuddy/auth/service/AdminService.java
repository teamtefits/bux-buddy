package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.user.UserResponse;
import com.buxbuddy.auth.dto.user.UserUpdateRequest;

import java.util.List;

public interface AdminService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String id);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(String id, UserUpdateRequest request);

    void updateUserStatus(String id, Boolean enabled);

    void deleteUser(String id);
}