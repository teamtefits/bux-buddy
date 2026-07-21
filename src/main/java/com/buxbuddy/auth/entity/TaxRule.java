package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.TaxCalculationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "tax_rule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String taxName;
    // GST, HST, CGST, SGST


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tax_type_id",
            nullable = false
    )
    private TaxType taxType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxCalculationType calculationType;
    @Column(nullable = false)
    private BigDecimal rate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "province_id",
            nullable = false
    )
    private Province province;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    @Builder.Default
    private Boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private ProductCategory category;
}