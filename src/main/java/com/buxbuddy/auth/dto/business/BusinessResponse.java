package com.buxbuddy.auth.dto.business;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BusinessResponse {

    private Long id;
    private String name;
    private String businessType;
    private String email;
    private String phone;
    private String address;
    private String status;
    private LocalDateTime createdAt;
}
