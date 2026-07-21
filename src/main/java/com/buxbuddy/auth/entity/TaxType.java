package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tax_type")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    // GST, HST, VAT, CGST, SGST
    @Column(nullable = false)
    private String name;
    // Goods and Services Tax
    private String description;
    @Builder.Default
    private Boolean active = true;
}