package com.buxbuddy.auth.dto.wholesaleProduct.stock;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStockHistoryRequest {

    private Long wholesaleProductId;

    private LocalDate stockDate;

    private BigDecimal openingStock;

    private BigDecimal stockAdded;

    private BigDecimal totalSales;

    private BigDecimal remainingStock;
}