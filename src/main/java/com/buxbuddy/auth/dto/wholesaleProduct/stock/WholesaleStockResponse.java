package com.buxbuddy.auth.dto.wholesaleProduct.stock;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStockResponse {

    private Long id;

    private Long businessId;

    private Long wholesaleProductId;

    private String productName;

    private BigDecimal currentStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}