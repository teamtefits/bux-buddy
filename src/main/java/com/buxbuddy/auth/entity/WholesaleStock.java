package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wholesale_stock",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_stock_business_product",
                        columnNames = {
                                "business_id",
                                "wholesale_product_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business that owns the wholesale stock.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            nullable = false
    )
    private Business business;

    /**
     * Wholesale product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "wholesale_product_id",
            nullable = false
    )
    private WholesaleProduct wholesaleProduct;

    /**
     * Current available wholesale stock.
     */
    @Column(
            name = "current_stock",
            precision = 12,
            scale = 3,
            nullable = false
    )
    @Builder.Default
    private BigDecimal currentStock = BigDecimal.ZERO;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}