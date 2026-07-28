package com.buxbuddy.auth.dto.Loyalty.customer;

import com.buxbuddy.auth.enums.CustomerTier;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoyaltyResponse {

    private Long customerId;
    private String customerName;
    private String phone;

    // Loyalty
    private Integer loyaltyPoints;
    private BigDecimal redeemableAmount;
    private CustomerTier tier;
    // Spending
    private Double monthlySpend;
    private Double lifetimeSpend;
    // Visit Analytics
    private Integer visitCount;
    private LocalDateTime firstVisit;
    private LocalDateTime lastVisit;
    // Customer Classification
    private String customerType; // VIP, Frequent, New, At Risk, Inactive
    // Address
    private String city;
    private String province;
    private String postalCode;
    private String country;
    // Birthday
    private Integer birthdayMonth;
    // Transaction Statistics
    private Integer totalTransactions;
    private Double averageOrderValue;
    // Business
    private Long businessId;
    // Dates
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}