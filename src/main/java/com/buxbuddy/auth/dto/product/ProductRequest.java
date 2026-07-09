package com.buxbuddy.auth.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    private String productName;

    private String itemCode;

    private String brand;

    private String department;

    private String batchNumber;

    private Double weight;

    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private Boolean gstApplicable;

    private Integer currentStock;

    private LocalDate expiryDate;

    private LocalDate deliveryDate;

    private Long vendorId;

    private Long businessId;
}