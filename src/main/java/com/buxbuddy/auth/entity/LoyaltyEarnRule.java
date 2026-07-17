package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.LoyaltyRuleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "loyalty_earn_rule",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_business_rule_type",
                        columnNames = {
                                "business_id",
                                "rule_type"
                        }
                )
        }
)
public class LoyaltyEarnRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    /*
      DEFAULT
      DAY
      VIP
      BIRTHDAY
      FIRST_VISIT
      CAMPAIGN
    */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoyaltyRuleType ruleType;

    /*
       1 = normal points
       2 = double points
       3 = triple points
    */
    @Column(nullable = false)
    private BigDecimal multiplier;
    /*
       Extra percentage bonus
       Example:
       Birthday +20%
    */
    private BigDecimal bonusPercentage;
    /*
       DAY rule
       MONDAY, TUESDAY...
    */
    private DayOfWeek dayOfWeek;
    /*
       Birthday month
       1-12
    */
    private Integer birthdayMonth;
    /*
       Minimum purchase required
       Example:
       Spend $50 get bonus
    */
    private BigDecimal minimumPurchaseAmount;
    /*
       Maximum points allowed
    */
    private Integer maxPoints;
    /*
       Rule start/end date
       Example:
       Christmas promotion
    */
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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