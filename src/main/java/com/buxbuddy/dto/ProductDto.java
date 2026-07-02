package com.buxbuddy.dto;

import com.buxbuddy.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    // Product Information
    private String productName;
    private String barcode;
    private String category;
    private Double weight;

    // Pricing
    private Double purchasePrice;
    private Double sellingPrice;

    // Inventory
    private Double stock;
    private Double reorderLevel;

    // Expiry
    private LocalDate expiryDate;

    // Created Date
    private LocalDateTime createdAt;

    private ProductStatus status;

    // Instead of Vendor object
    private Long vendorId;
}