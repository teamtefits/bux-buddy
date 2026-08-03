package com.buxbuddy.auth.dto.wholesaleProduct.stock;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStockRequest {

    @NotNull(message = "Wholesale product ID is required")
    private Long wholesaleProductId;

    @NotNull(message = "Current stock is required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Stock cannot be negative"
    )
    private BigDecimal currentStock;
}