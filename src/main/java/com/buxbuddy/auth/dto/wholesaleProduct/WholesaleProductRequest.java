package com.buxbuddy.auth.dto.wholesaleProduct;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WholesaleProductRequest {

    private Long businessId;

    private String productName;

    private BigDecimal costPrice;

    private BigDecimal wholesalePrice;

    private Boolean active;
}