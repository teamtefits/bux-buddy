package com.buxbuddy.auth.dto.Loyalty.redeem;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Data
@Builder
public class LoyaltyRedeemResponse {

    private String customerName;
    private Integer redeemedPoints;
    private BigDecimal redeemedAmount;
    private Integer remainingPoints;
    private BigDecimal remainingRedeemableAmount;
    private String message;
}