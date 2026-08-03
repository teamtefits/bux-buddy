package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "wholesale_sale",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_sale_business_invoice",
                        columnNames = {
                                "business_id",
                                "invoice_number"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            nullable = false
    )
    private Business business;

    /**
     * Shop/customer receiving the wholesale products.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shop_id",
            nullable = false
    )
    private WholesaleShop shop;

    /**
     * Person/partner who handled or took the products.
     * Can be null when the business owner handles the sale.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private WholesalePartner partner;

    @Column(
            name = "invoice_number",
            nullable = false
    )
    private String invoiceNumber;

    @Column(
            name = "sale_date",
            nullable = false
    )
    private LocalDate saleDate;

    /**
     * Total selling amount.
     */
    @Column(
            name = "total_amount",
            precision = 14,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Total cost of products.
     */
    @Column(
            name = "total_cost",
            precision = 14,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal totalCost = BigDecimal.ZERO;

    /**
     * Total profit.
     *
     * totalAmount - totalCost
     */
    @Column(
            name = "total_profit",
            precision = 14,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(
            name = "notes",
            length = 500
    )
    private String notes;

    @OneToMany(
            mappedBy = "sale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<WholesaleSaleItem> items = new ArrayList<>();

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