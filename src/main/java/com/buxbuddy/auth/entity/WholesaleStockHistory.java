package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wholesale_stock_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_stock_business_product_date",
                        columnNames = {
                                "business_id",
                                "wholesale_product_id",
                                "stock_date"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleStockHistory {

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
     * Stock history date.
     */
    @Column(
            name = "stock_date",
            nullable = false
    )
    private LocalDate stockDate;

    /**
     * Stock available at the beginning of the day.
     */
    @Column(
            name = "opening_stock",
            precision = 12,
            scale = 3,
            nullable = false
    )
    @Builder.Default
    private BigDecimal openingStock = BigDecimal.ZERO;

    /**
     * New stock added during the day.
     */
    @Column(
            name = "stock_added",
            precision = 12,
            scale = 3,
            nullable = false
    )
    @Builder.Default
    private BigDecimal stockAdded = BigDecimal.ZERO;

    /**
     * Total quantity sold/taken during the day.
     */
    @Column(
            name = "total_sales",
            precision = 12,
            scale = 3,
            nullable = false
    )
    @Builder.Default
    private BigDecimal totalSales = BigDecimal.ZERO;

    /**
     * End-of-day remaining stock.
     *
     * openingStock + stockAdded - totalSales
     */
    @Column(
            name = "remaining_stock",
            precision = 12,
            scale = 3,
            nullable = false
    )
    @Builder.Default
    private BigDecimal remainingStock = BigDecimal.ZERO;

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