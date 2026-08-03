package com.buxbuddy.auth.dto.wholesaleProduct.stock;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStockHistoryResponse {

    private Long id;

    private Long businessId;

    private Long wholesaleProductId;

    private String productName;

    private LocalDate stockDate;

    private BigDecimal openingStock;

    private BigDecimal stockAdded;

    private BigDecimal totalSales;

    private BigDecimal remainingStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}