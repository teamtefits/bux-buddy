package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.FeedbackCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_feedback")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer who provided feedback
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;

    // Rating 1 - 5 stars
    @Column(nullable = false)
    private Integer rating;

    // Customer feedback message
    @Column(length = 2000)
    private String feedback;

    // REVIEW, COMPLAINT, SUGGESTION, COMPLIMENT
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackCategory category;

    // OPEN, IN_PROGRESS, RESOLVED
    @Builder.Default
    @Column(nullable = false)
    private String status = "OPEN";

    // Response from business
    @Column(length = 2000)
    private String response;

    // Who handled the feedback
    private String respondedBy;

    private LocalDateTime respondedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}