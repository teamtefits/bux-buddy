package com.buxbuddy.auth.dto.wholesaleProduct.shop;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WholesaleShopRequest {
    private Long businessId;
    private String shopName;
    private String contactName;
    private String phone;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String postalCode;
    private Boolean active;
}