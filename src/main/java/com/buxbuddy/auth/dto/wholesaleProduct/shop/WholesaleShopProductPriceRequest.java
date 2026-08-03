package com.buxbuddy.auth.dto.wholesaleProduct.shop;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WholesaleShopProductPriceRequest {

    private Long shopId;
    private Long wholesaleProductId;
    private BigDecimal sellingPrice;
    private Boolean active;
}