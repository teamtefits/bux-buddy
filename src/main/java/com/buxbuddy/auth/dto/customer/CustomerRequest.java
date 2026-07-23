package com.buxbuddy.auth.dto.customer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String customerName;
    private String phone;
    // Login information
    private String email;
    private String password;
    // Address Information
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private Integer birthdayMonth;
    private Long businessId;
}