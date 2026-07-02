package com.buxbuddy.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private Long id;

    private String username;

    private String email;

    private String role;

    private String phoneNumber;

    private String password;

    private LocalDateTime createdAt;

}

