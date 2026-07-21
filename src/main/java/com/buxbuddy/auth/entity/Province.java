package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "province",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_province_country",
                        columnNames = {
                                "province_code",
                                "country_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String provinceName;
    @Column(nullable = false)
    private String provinceCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "country_id",
            nullable = false
    )
    private Country country;
}