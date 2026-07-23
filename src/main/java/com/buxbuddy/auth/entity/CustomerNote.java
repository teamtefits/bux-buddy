package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.NoteType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_note")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Customer related to this note
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;
    // Type of note
    // DELIVERY_NOTE, INTERNAL_NOTE, PREFERENCE, CUSTOMER_COMMENT
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteType noteType;
    // Note content
    @Column(
            nullable = false,
            length = 1000
    )
    private String note;
    // User/staff who created the note
    private String createdBy;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}