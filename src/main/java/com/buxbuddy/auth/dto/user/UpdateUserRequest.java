package com.buxbuddy.auth.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private String phone;
}