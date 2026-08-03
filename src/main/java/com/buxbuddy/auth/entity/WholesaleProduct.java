package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wholesale_product",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_product_business_name",
                        columnNames = {
                                "business_id",
                                "product_name"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            nullable = false
    )
    private Business business;

    @Column(
            name = "product_name",
            nullable = false
    )
    private String productName;

    /**
     * Your purchase/cost price.
     */
    @Column(
            name = "cost_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    @Builder.Default
    private BigDecimal costPrice = BigDecimal.ZERO;

    /**
     * Default wholesale selling price.
     */
    @Column(
            name = "wholesale_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    @Builder.Default
    private BigDecimal wholesalePrice = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

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