package com.buxbuddy.auth.dto.Loyalty.earn;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyRedeemRuleResponse {

    private Long id;
    private Integer pointsRequired;
    private BigDecimal discountValue;
    private String type;
    private Boolean active;
    private Long businessId;
}