package com.buxbuddy.auth.dto.Loyalty.earn;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyEarnResponse {
    private String customerName;
    private BigDecimal purchaseAmount;
    private Integer earnedPoints;
    private BigDecimal rewardValue;
    private Integer totalPoints;
    private String message;
}