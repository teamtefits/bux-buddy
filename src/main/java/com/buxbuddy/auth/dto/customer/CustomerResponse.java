package com.buxbuddy.auth.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import com.buxbuddy.auth.enums.CustomerTier;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Long customerId;
    private String customerName;
    private String email;
    private String phone;
    private Integer birthdayMonth;
    private Integer loyaltyPoints;
    private Integer visitCount;
    private LocalDateTime lastVisit;
    private CustomerTier tier;
    private Double monthlySpend;
    private Double lifetimeSpend;
    private Long businessId;
    private String businessName;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}