package com.buxbuddy.auth.dto.Loyalty.earn;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyEarnRequest {
    private String phone;
    private BigDecimal purchaseAmount;
}