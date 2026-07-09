package com.buxbuddy.auth.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_reward_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyRewardRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Points customer needs
    @Column(nullable = false)
    private Integer pointsRequired;
    // Discount amount
    @Column(nullable = false)
    private Double discountValue;
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