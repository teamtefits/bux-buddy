package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wholesale_shop_product_price",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_shop_product",
                        columnNames = {
                                "shop_id",
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
public class WholesaleShopProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shop_id",
            nullable = false
    )
    private WholesaleShop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "wholesale_product_id",
            nullable = false
    )
    private WholesaleProduct wholesaleProduct;

    @Column(
            name = "selling_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal sellingPrice;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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