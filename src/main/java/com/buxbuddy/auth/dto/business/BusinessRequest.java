package com.buxbuddy.auth.dto.business;

import com.buxbuddy.auth.enums.BusinessCategoryType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessRequest {
    // Business Information
    private String businessName;
    private BusinessCategoryType businessCategory;
    // Contact Information
    private String businessEmail;
    private String businessPhone;
    // Address Information
    private String addressLine1;
    private String addressLine2;
    private String city;
    private Long provinceId;
    private String postalCode;
    // Tax Information
    private String taxNumber;
    private Boolean taxEnabled = true;
    // Owner Account Information
    private String password;
}