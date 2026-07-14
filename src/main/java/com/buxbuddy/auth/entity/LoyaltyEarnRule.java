package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_earn_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(nullable = false)
    private String ruleType;
    /*
       1 = normal points
       2 = double points
       3 = triple points
    */
    @Column(nullable = false)
    private Integer multiplier;
    /*
       Extra percentage bonus
       Example:
       Birthday +20%
    */
    private Integer bonusPercentage;
    /*
       DAY rule
       MONDAY, TUESDAY...
    */
    private String dayOfWeek;
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
    private Double minimumPurchaseAmount;
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