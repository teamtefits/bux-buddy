package com.buxbuddy.auth.dto.customer;
import lombok.Data;
import java.util.UUID;

@Data
public class CustomerRequest {
    private String name;
    private String phone;
    private Long businessId;
    private UUID userId;
    private String email;
    private String password;
}