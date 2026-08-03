package com.buxbuddy.auth.dto.sales;


import com.buxbuddy.auth.enums.ReconciliationStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPaymentReconciliationResponse {

    private Long id;
    private Long businessId;
    private LocalDate reconciliationDate;
    private BigDecimal invoiceAmount;
    private BigDecimal batchAmount;
    private BigDecimal cashAmount;
    private BigDecimal interacAmount;
    private BigDecimal differenceAmount;
    private ReconciliationStatus status;
}