package com.buxbuddy.auth.dto.customer;
import com.buxbuddy.auth.enums.RoleType;
import lombok.Data;

import java.math.BigInteger;
import java.util.UUID;

import com.buxbuddy.auth.enums.CustomerTier;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    private Long id;
    private String customerName;
    private String phone;
    private String email;
    private String password;
    private Integer birthdayMonth;
    private Integer loyaltyPoints;
    private Integer visitCount;
    private LocalDateTime lastVisit;
    private CustomerTier tier;
    private Double monthlySpend;
    private Double lifetimeSpend;
    private Long businessId;
    private String businessName;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}