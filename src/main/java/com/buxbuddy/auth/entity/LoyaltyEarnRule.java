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
      PRODUCT
      MEMBERSHIP
    */
    @Column(nullable = false)
    private String ruleType;
    /*
      Example:

      Normal = 1
      Tuesday = 3
      VIP = 5
    */
    @Column(nullable = false)
    private Integer pointMultiplier;
    /*
      MONDAY
      TUESDAY
      etc.

      Only for DAY rules
    */
    private String dayOfWeek;
    /*
      Is rule active?
    */
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