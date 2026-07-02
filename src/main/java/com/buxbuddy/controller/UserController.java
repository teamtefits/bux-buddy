package com.buxbuddy.controller;

import com.buxbuddy.dto.ApiResponse;
import com.buxbuddy.dto.LoginRequest;
import com.buxbuddy.dto.RegisterRequest;
import com.buxbuddy.entity.User;
import com.buxbuddy.repository.UserRepository;
import com.buxbuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {

        User user = userService.register(request);

        return new ApiResponse<>(
                "User registered successfully",
                user
        );
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            String token = userService.login(request);

            return ResponseEntity.ok(
                    Map.of("token", token)
            );

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(401)
                    .body(ex.getMessage());
        }
    }
}