package com.buxbuddy.auth.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private Long customerID;
    private String customerName;
    private String email;
    private String phone;
    private String password;
    private Long businessId;
    private LocalDateTime createdAt;
}