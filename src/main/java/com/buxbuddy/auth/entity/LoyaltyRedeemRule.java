package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(
        name = "loyalty_redeem_rule",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_business_points",
                        columnNames = {
                                "business_id",
                                "points_required"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyRedeemRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer pointsRequired;

    @Column(nullable = false)
    private BigDecimal discountValue;

    /*
       FIXED
       PERCENT
    */
    @Column(nullable = false)
    private String type;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_id", nullable=false)
    private Business business;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
}