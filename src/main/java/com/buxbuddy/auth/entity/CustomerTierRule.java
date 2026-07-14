package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_tier_rule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTierRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tierName;
    /*
       NORMAL
       BRONZE
       SILVER
       GOLD
       VIP
    */
    private Double minimumSpend;
    private Integer minimumVisits;
    @Enumerated(EnumType.STRING)
    private CustomerTier tier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_id")
    private Business business;
    private Boolean active = true;
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
