package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.user.UserResponse;
import com.buxbuddy.auth.dto.user.UserUpdateRequest;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.repository.UserRepository;
import com.buxbuddy.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {

        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public void updateUserStatus(String id, Boolean enabled) {

        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(enabled);

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Soft delete
        user.setEnabled(false);

        userRepository.save(user);

        // If you ever want permanent delete:
        // userRepository.delete(user);
    }
    private UserResponse mapToResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.getEnabled())
                .build();
    }
}