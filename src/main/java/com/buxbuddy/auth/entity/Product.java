package com.buxbuddy.auth.entity;


import com.buxbuddy.auth.enums.PackageType;
import com.buxbuddy.auth.enums.StockStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Entity
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_business_brand_batch",
                        columnNames = {
                                "business_id",
                                "product_name",
                                "brand",
                                "batch_number"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // ==========================
    // Product Information
    // ==========================
    @Column(
            name = "product_name",
            nullable = false
    )
    private String productName;
    @Column(
            name = "product_Code",
            nullable = false
    )
    private String productCode;
    @Column(
            unique = false
    )
    private String brand;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private ProductCategory category;
    @Column(
            name = "batch_number",
            nullable = false
    )
    private String batchNumber;
    // ==========================
    // Size / Packaging
    // ==========================

    // Weight in KG
    private Double weight;
    // Volume in Liter
    // Used for deposit calculation
    private Double volume;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    // ==========================
    // Pricing
    // ==========================
    @Column(nullable = false)
    private BigDecimal cost;
    @Column(nullable = false)
    private BigDecimal retailPrice;
    @Column(nullable = false)
    private BigDecimal margin;
    @Column(nullable = false)
    private BigDecimal marginPercentage;
    // ==========================
    // Tax
    // ==========================
    @Builder.Default
    @Column(nullable = false)
    private Boolean taxApplicable = true;
    // ==========================
    // Inventory
    // ==========================
    @Column(nullable = false)
    private Integer currentStock;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus stockStatus;
    private LocalDate expiryDate;
    private LocalDate deliveryDate;
    // ==========================
    // Relationships
    // ==========================
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vendor_id",
            nullable = false
    )
    private Vendor vendor;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            nullable = false
    )
    private Business business;
    // ==========================
    // Status
    // ==========================
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
    // ==========================
    // Margin Calculation
    // ==========================
    @PrePersist
    @PreUpdate
    private void calculateMargin() {
        if (cost != null && retailPrice != null) {
            this.margin =
                    retailPrice.subtract(cost);
            if (cost.compareTo(BigDecimal.ZERO) > 0) {
                this.marginPercentage =
                        this.margin
                                .divide(
                                        cost,
                                        4,
                                        RoundingMode.HALF_UP
                                )
                                .multiply(
                                        BigDecimal.valueOf(100)
                                );
            } else {
                this.marginPercentage =
                        BigDecimal.ZERO;
            }
        } else {
            this.margin = BigDecimal.ZERO;
            this.marginPercentage = BigDecimal.ZERO;
        }
    }
}