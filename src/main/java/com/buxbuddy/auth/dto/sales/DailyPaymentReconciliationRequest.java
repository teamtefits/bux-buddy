package com.buxbuddy.auth.dto.sales;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPaymentReconciliationRequest {

    private Long businessId;

    private LocalDate reconciliationDate;

    private BigDecimal invoiceAmount;

    private BigDecimal batchAmount;

    private BigDecimal cashAmount;

    private BigDecimal interacAmount;
}