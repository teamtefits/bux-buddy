package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone")
        }
)
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

    @Column(nullable = false, unique = true)
    private String phone;

    // Address Information
    private String addressLine1;

    private String addressLine2;

    private String city;

    private String province;

    private String postalCode;

    private String country;

    private Integer birthdayMonth;

    @Builder.Default
    private Integer loyaltyPoints = 0;

    @Builder.Default
    private Integer visitCount = 0;

    private LocalDateTime lastVisit;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerTier tier = CustomerTier.NORMAL;

    @Builder.Default
    private Double monthlySpend = 0.0;

    @Builder.Default
    private Double lifetimeSpend = 0.0;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // Login connection
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    // Loyalty transactions
    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LoyaltyTransaction> transactions = new ArrayList<>();


    // Customer notes
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<CustomerNote> notes = new ArrayList<>();


    // Customer feedback
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<CustomerFeedback> feedbacks = new ArrayList<>();
}