package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.api.ApiResponseBuilder;
import com.buxbuddy.auth.dto.api.ApiSuccessResponse;
import com.buxbuddy.auth.dto.login.RegisterRequest;
import com.buxbuddy.auth.dto.login.RegisterResponse;
import com.buxbuddy.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    /*
    User registration
     * */
    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<RegisterResponse>> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        RegisterResponse response = userService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseBuilder.success(
                                HttpStatus.CREATED.value(),
                                "User registered successfully",
                                response,
                                httpRequest
                        )
                );
    }
    /*
        User login
    * */
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<RegisterResponse>> login(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {

        RegisterResponse response = userService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseBuilder.success(
                                HttpStatus.OK.value(),
                                "Login successful",
                                response,
                                httpRequest
                        )
                );
    }
}