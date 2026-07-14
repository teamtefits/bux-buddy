package com.buxbuddy.auth.dto.menu;

import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequest {
    private String name;
    private String path;
    private String icon;
    private Integer displayOrder;
    private boolean active;
    private BusinessCategoryType businessCategory;
    private RoleType role;
    private Long parentId;
}