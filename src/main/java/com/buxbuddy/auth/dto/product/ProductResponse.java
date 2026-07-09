package com.buxbuddy.auth.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String productName;

    private String itemCode;

    private String brand;

    private String department;

    private String batchNumber;

    private Double weight;

    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private BigDecimal margin;

    private Boolean gstApplicable;

    private Integer currentStock;

    private String stockStatus;

    private LocalDate expiryDate;

    private LocalDate deliveryDate;

    private String vendorName;

    private Long vendorId;

    private Long businessId;

    private BigDecimal marginPercentage;
}