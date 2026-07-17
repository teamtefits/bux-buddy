package com.buxbuddy.auth.dto.Loyalty.customer;

import com.buxbuddy.auth.enums.CustomerTier;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoyaltyResponse {
    private Long customerId;
    private String customerName;
    private String phone;
    private Integer loyaltyPoints;
    private CustomerTier tier;
    private Long businessId;
}