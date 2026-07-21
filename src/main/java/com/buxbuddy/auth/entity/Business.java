package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "business",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_business_name_location",
                        columnNames = {
                                "business_name",
                                "city",
                                "province_id",
                                "postal_code"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String businessName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private BusinessCategory businessCategory;
    // Contact Information
    @Column(nullable = false)
    private String businessEmail;
    @Column(nullable = false)
    private String businessPhone;
    // Address Information
    private String addressLine1;
    private String addressLine2;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String postalCode;
    // Province -> Country relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "province_id",
            nullable = false
    )
    private Province province;
    // Tax Information
    private String taxNumber;
    // GST/HST number, VAT number, etc.
    @Builder.Default
    private Boolean taxEnabled = true;
    // Business Status
    @Builder.Default
    private String status = "ACTIVE";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(
            mappedBy = "business",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Customer> customers = new ArrayList<>();
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}