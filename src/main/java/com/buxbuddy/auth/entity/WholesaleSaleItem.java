package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "wholesale_sale_item"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleSaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parent wholesale sale / invoice.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sale_id",
            nullable = false
    )
    private WholesaleSale sale;

    /**
     * Wholesale product being sold.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "wholesale_product_id",
            nullable = false
    )
    private WholesaleProduct wholesaleProduct;

    /**
     * Quantity sold.
     */
    @Column(
            name = "quantity",
            precision = 12,
            scale = 3,
            nullable = false
    )
    private BigDecimal quantity;

    /**
     * Cost price at the time of sale.
     *
     * We store this here because the cost price
     * may change later.
     */
    @Column(
            name = "cost_price",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal costPrice;

    /**
     * Actual selling price used for this sale.
     *
     * This can be the default wholesale price
     * or a special shop price.
     */
    @Column(
            name = "selling_price",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal sellingPrice;

    /**
     * quantity × costPrice
     */
    @Column(
            name = "total_cost",
            precision = 14,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalCost;

    /**
     * quantity × sellingPrice
     */
    @Column(
            name = "total_amount",
            precision = 14,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalAmount;

    /**
     * totalAmount - totalCost
     */
    @Column(
            name = "profit",
            precision = 14,
            scale = 2,
            nullable = false
    )
    private BigDecimal profit;
}