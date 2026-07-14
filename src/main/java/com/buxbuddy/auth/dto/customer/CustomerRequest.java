package com.buxbuddy.auth.dto.customer;
import com.buxbuddy.auth.enums.RoleType;
import lombok.Data;
import java.util.UUID;

@Data
public class CustomerRequest {
    private String customerName;
    private String email;
    private String phone;
    private String password;
    private Long businessId;
}