package com.buxbuddy.auth.entity;


import com.buxbuddy.auth.enums.StockAction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    // Stock before the transaction
    private Integer previousStock;

    // Stock after the transaction
    private Integer newStock;

    // Quantity changed (+/-)
    private Integer quantityChanged;

    @Enumerated(EnumType.STRING)
    private StockAction action;

    // Optional reason or notes
    private String remarks;

    // Keep price at the time of the transaction
    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private LocalDateTime createdAt;
}