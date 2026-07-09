package com.buxbuddy.auth.entity;

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
    private String name;
    @Column(nullable=false)
    private String phone;
    // Loyalty balance
    @Builder.Default
    private Integer loyaltyPoints = 0;
    @Builder.Default
    private Integer visitCount = 0;
    private LocalDateTime lastVisit;
    // Login connection
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    // Business
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_id")
    private Business business;
    // ADD THIS
    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LoyaltyTransaction> transactions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}