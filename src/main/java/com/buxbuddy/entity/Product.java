package com.buxbuddy.entity;

import com.buxbuddy.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product Information
    private String productName;
    private String barcode;
    private String category;
    private Double weight;

    // 💰 Pricing
    private Double purchasePrice;
    private Double sellingPrice;
    // 📦 Stock
    // Inventory
    private Double stock;
    private Double reorderLevel;
    // Expiry
    private LocalDate expiryDate;
    //Stock Date
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    // getters and setters
}
