package com.buxbuddy.auth.dto.business;

import com.buxbuddy.auth.enums.RoleType;
import lombok.Data;

@Data
public class BusinessRequest {

    private String businessName;
    private String businessCategory;
    private String businessEmail;
    private String businessPhone;
    private String businessAddress;
    private String password;
    private RoleType role;
}