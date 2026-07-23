package com.buxbuddy.auth.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {

    private Long customerId;
    private String customerName;
    private String phone;
    private String email;
    // Address
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    // Marketing / Personalization
    private Integer birthdayMonth;
    // Business connection
    private Long businessId;
    private String businessName;
    // Login connection
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}