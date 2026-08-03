package com.buxbuddy.auth.entity;


import com.buxbuddy.auth.enums.ReconciliationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "daily_payment_reconciliation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_business_reconciliation_date",
                        columnNames = {
                                "business_id",
                                "reconciliation_date"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPaymentReconciliation {

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
            name = "reconciliation_date",
            nullable = false
    )
    private LocalDate reconciliationDate;

    @Column(
            name = "invoice_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal invoiceAmount = BigDecimal.ZERO;

    @Column(
            name = "batch_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal batchAmount = BigDecimal.ZERO;

    @Column(
            name = "cash_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal cashAmount = BigDecimal.ZERO;

    @Column(
            name = "interac_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal interacAmount = BigDecimal.ZERO;

    @Column(
            name = "difference_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    @Builder.Default
    private BigDecimal differenceAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private ReconciliationStatus status =
            ReconciliationStatus.BALANCED;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
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