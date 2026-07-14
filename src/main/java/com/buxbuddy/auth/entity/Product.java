package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.StockStatus;
import jakarta.persistence.*;
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

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String department;

    @Column(name = "batch_number", nullable = false)
    private String batchNumber;

    private Double weight;

    @Column(nullable = false)
    private BigDecimal wholesalePrice;

    @Column(nullable = false)
    private BigDecimal retailPrice;

    @Column(nullable = false)
    private BigDecimal margin;

    @Column(nullable = false)
    private Boolean gstApplicable;

    @Column(nullable = false)
    private Integer currentStock;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    private LocalDate expiryDate;

    private LocalDate deliveryDate;

    @Column(nullable = false)
    private BigDecimal marginPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @PrePersist
    @PreUpdate
    private void calculateMargin() {
        if (wholesalePrice != null && retailPrice != null) {
            // Margin amount
            this.margin = retailPrice.subtract(wholesalePrice);
            // Margin percentage
            if (wholesalePrice.compareTo(BigDecimal.ZERO) > 0) {
                this.marginPercentage = this.margin
                        .divide(wholesalePrice, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                this.marginPercentage = BigDecimal.ZERO;
            }
        } else {
            this.margin = BigDecimal.ZERO;
            this.marginPercentage = BigDecimal.ZERO;
        }
    }
}