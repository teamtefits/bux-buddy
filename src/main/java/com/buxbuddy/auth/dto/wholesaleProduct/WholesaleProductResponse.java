package com.buxbuddy.auth.dto.wholesaleProduct;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WholesaleProductResponse {

    private Long id;

    private Long businessId;

    private String productName;

    private BigDecimal costPrice;

    private BigDecimal wholesalePrice;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}