package com.buxbuddy.auth.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProductResponse {
    private Long id;
    // Product Information
    private String productName;
    private String itemCode;
    private String brand;
    // Category
    private Long categoryId;
    private String categoryName;
    private String batchNumber;
    private Double weight;
    // Pricing
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    // Margin
    private BigDecimal margin;
    private BigDecimal marginPercentage;
    // Tax
    private BigDecimal gstAmount;
    private BigDecimal provincialTaxAmount;
    private BigDecimal carbonTaxAmount;
    private BigDecimal totalTax;
    // Deposit
    private Boolean depositApplicable;
    private BigDecimal depositAmount;
    // Final Price
    private BigDecimal finalPrice;
    // Stock
    private Integer currentStock;
    private String stockStatus;
    private LocalDate expiryDate;
    private LocalDate deliveryDate;
    // Vendor
    private Long vendorId;
    private String vendorName;
    // Business
    private Long businessId;
}
