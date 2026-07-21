package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity@Table(
        name = "country",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_country_code",
                        columnNames = "countryCode"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String countryName;

    @Column(nullable = false, unique = true)
    private String countryCode;


    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Province> provinces = new ArrayList<>();
}