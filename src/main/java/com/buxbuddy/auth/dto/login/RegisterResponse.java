package com.buxbuddy.auth.dto.login;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RegisterResponse {

    private UUID userId;
    private String email;
    private String message;
    private String token;
    private List<String> role;
}