package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "wholesale_shop",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wholesale_shop_business_name",
                        columnNames = {
                                "business_id",
                                "shop_name"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholesaleShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business that owns this wholesale shop/customer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            nullable = false
    )
    private Business business;

    /**
     * Shop / customer name.
     */
    @Column(
            name = "shop_name",
            nullable = false,
            length = 150
    )
    private String shopName;

    @Column(
            name = "contact_name",
            length = 100
    )
    private String contactName;

    @Column(
            name = "phone",
            length = 30
    )
    private String phone;

    @Column(
            name = "email",
            length = 150
    )
    private String email;

    @Column(
            name = "address_line1",
            length = 255
    )
    private String addressLine1;

    @Column(
            name = "address_line2",
            length = 255
    )
    private String addressLine2;

    @Column(
            name = "city",
            length = 100
    )
    private String city;

    @Column(
            name = "province",
            length = 100
    )
    private String province;

    @Column(
            name = "postal_code",
            length = 20
    )
    private String postalCode;

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}