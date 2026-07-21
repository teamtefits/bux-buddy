package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "deposit_rule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal amount;
    private Double minimumVolume;
    private Double maximumVolume;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
    @Column(nullable = false)
    private Boolean depositApplicable = true;
    @Column(nullable = false)
    private Boolean active = true;
}