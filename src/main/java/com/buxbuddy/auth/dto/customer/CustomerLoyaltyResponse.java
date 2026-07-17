package com.buxbuddy.auth.dto.customer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoyaltyResponse {
    private Long id;
    private String customerName;
    private String phone;
    private Integer loyaltyPoints;
}