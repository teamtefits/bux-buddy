package com.buxbuddy.auth.dto.product;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Data
@Builder
public class TaxCalculationResponse {
    // GST amount
    private BigDecimal gstAmount;
    // PST / HST / Sales Tax amount
    private BigDecimal provincialTaxAmount;
    // Carbon tax amount
    private BigDecimal carbonTaxAmount;
    // GST + Provincial + Carbon
    private BigDecimal totalTax;
    // Deposit information
    private Boolean depositApplicable;
    private BigDecimal depositAmount;
    // Retail Price + Tax + Deposit
    private BigDecimal finalPrice;
}
