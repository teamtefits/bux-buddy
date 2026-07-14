package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    @Column(nullable=false)
    private String phone;
    private Integer birthdayMonth;
    @Builder.Default
    private Integer loyaltyPoints = 0;
    @Builder.Default
    private Integer visitCount = 0;
    private LocalDateTime lastVisit;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerTier tier = CustomerTier.NORMAL;
    private Double monthlySpend;
    private Double lifetimeSpend;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_id")
    private Business business;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Login connection
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    // Loyalty transactions
    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LoyaltyTransaction> transactions = new ArrayList<>();

}