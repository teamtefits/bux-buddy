package com.buxbuddy.auth.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private Integer loyaltyPoints;
    private Integer visitCount;
    private LocalDateTime lastVisit;
    private Long businessId;
    private UUID userId;
    private LocalDateTime createdAt;

}