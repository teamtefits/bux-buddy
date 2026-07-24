package com.buxbuddy.auth.dto.product;

import com.buxbuddy.auth.enums.PackageType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    // Product Information
    private String productName;
    private String productCode;
    private String brand;
    private Long categoryId;
    private String batchNumber;
    // Size / Packaging
    private Double weight;
    private Double volume;
    private PackageType packageType;
    // Pricing
    private BigDecimal cost;
    private BigDecimal retailPrice;
    // Tax
    private Boolean taxApplicable;
    // Stock
    private Integer currentStock;
    // Dates
    private LocalDate expiryDate;
    private LocalDate deliveryDate;
    // Relations
    private Long vendorId;
    private Long businessId;
}