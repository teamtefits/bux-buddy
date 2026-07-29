package com.buxbuddy.auth.dto.Loyalty.redeem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoyaltyRedeemRequest {

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Redeem amount is required")
    @DecimalMin(value = "0.01", message = "Redeem amount must be greater than zero")
    private BigDecimal redeemAmount;
}