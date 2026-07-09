package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "vendor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vendor_name",
                        columnNames = "vendor_name"
                ),
                @UniqueConstraint(
                        name = "uk_vendor_email",
                        columnNames = "vendor_email"
                ),
                @UniqueConstraint(
                        name = "uk_vendor_phone",
                        columnNames = "vendor_phone"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String vendorName;
    @Column(unique = true)
    private String vendorEmail;
    @Column(unique = true)
    private String vendorPhone;
    private String vendorAddress;
    @Builder.Default
    private String status = "ACTIVE";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}