package com.buxbuddy.auth.dto.login;

import com.buxbuddy.auth.enums.RoleType;
import lombok.Data;

@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private RoleType role;
}