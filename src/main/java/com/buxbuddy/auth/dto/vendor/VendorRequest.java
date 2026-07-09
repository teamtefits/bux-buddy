package com.buxbuddy.auth.dto.vendor;

import lombok.Data;

@Data
public class VendorRequest {

    private String name;
    private String email;
    private String phone;
    private String address;
    private Long businessId;
}