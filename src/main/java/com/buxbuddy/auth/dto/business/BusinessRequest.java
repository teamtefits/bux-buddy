package com.buxbuddy.auth.dto.business;

import lombok.Data;

@Data
public class BusinessRequest {

    private String name;
    private String businessType;
    private String email;
    private String phone;
    private String address;
    private String password;
}