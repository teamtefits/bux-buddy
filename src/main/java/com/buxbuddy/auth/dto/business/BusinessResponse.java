package com.buxbuddy.auth.dto.business;

import com.buxbuddy.auth.enums.BusinessCategoryType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BusinessResponse {

    private Long id;
    // Business Information
    private String name;
    private BusinessCategoryType businessType;
    // Contact Information
    private String email;
    private String phone;
    // Address Information
    private String addressLine1;
    private String addressLine2;
    private String city;
    private ProvinceResponse province;
    private String postalCode;
    // Tax Information
    private String taxNumber;
    private Boolean taxEnabled;
    // Status
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
