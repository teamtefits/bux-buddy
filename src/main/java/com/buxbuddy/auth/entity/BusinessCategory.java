package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.BusinessCategoryType;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private BusinessCategoryType name;
    private String description;
    @Builder.Default
    private Boolean active = true;
}