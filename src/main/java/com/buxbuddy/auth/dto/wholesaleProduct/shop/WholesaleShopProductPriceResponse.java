package com.buxbuddy.auth.dto.wholesaleProduct.shop;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WholesaleShopProductPriceResponse {

    private Long id;
    private Long shopId;
    private String shopName;
    private Long wholesaleProductId;
    private String productName;
    private BigDecimal sellingPrice;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
