package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.LoyaltyTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private LoyaltyTransactionType transactionType;

    private Integer points;

    // Balance after transaction
    private Integer balanceAfterTransaction;

    // Purchase amount
    private BigDecimal purchaseAmount;

    // Redeem amount
    private BigDecimal redeemValue;

    private String description;

    private String referenceNo;

    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;
    private LocalDateTime createdDate;
}