package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pre_order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /*
       PENDING
       CONFIRMED
       READY
       COMPLETED
       CANCELLED
    */
    @Column(nullable = false)
    private String status;
    private LocalDateTime pickupDate;
    private String notes;

    /*
       Customer who placed order
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    /*
       Store receiving order
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    @OneToMany(
            mappedBy = "preOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PreOrderItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}