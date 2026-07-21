package com.buxbuddy.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_category",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_category_name",
                        columnNames = "category_name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "category_name",
            nullable = false,
            unique = true
    )
    private String categoryName;
    @Column
    private String description;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}