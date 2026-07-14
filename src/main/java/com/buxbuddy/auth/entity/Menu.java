package com.buxbuddy.auth.entity;

import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.CustomerTier;
import com.buxbuddy.auth.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "menus",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_category_role_name",
                        columnNames = {
                                "name",
                                "business_category",
                                "role"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String path;
    private String icon;
    private Integer displayOrder;
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessCategoryType businessCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Menu parent;
}