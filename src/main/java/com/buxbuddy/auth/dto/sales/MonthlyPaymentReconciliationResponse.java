package com.buxbuddy.auth.dto.sales;


import com.buxbuddy.auth.enums.ReconciliationStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyPaymentReconciliationResponse {

    private Long businessId;
    private int year;
    private int month;
    private LocalDate startDate;
    private LocalDate endDate;
    // Monthly totals
    private BigDecimal totalInvoiceAmount;
    private BigDecimal totalBatchAmount;
    private BigDecimal totalCashAmount;
    private BigDecimal totalInteracAmount;
    private BigDecimal totalPaymentAmount;
    private BigDecimal totalDifferenceAmount;
    private ReconciliationStatus status;
    // Daily records
    private List<DailyPaymentReconciliationResponse> dailyRecords;
}