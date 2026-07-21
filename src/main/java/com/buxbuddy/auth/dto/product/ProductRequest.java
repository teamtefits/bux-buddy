package com.buxbuddy.auth.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    // Product Information
    private String productName;
    private String itemCode;
    private String brand;
    private Long categoryId;
    private String batchNumber;
    private Double weight;
    // Pricing
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    // Stock
    private Integer currentStock;
    // Dates
    private LocalDate expiryDate;
    private LocalDate deliveryDate;
    // Relations
    private Long vendorId;
    private Long businessId;
}