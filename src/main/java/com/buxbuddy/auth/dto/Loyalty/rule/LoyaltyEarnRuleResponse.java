package com.buxbuddy.auth.dto.Loyalty.rule;
import com.buxbuddy.auth.enums.LoyaltyRuleType;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyEarnRuleResponse {

    private Long id;
    private String name;
    private LoyaltyRuleType ruleType;
    private BigDecimal multiplier;
    private BigDecimal bonusPercentage;
    private DayOfWeek dayOfWeek;
    private Integer birthdayMonth;
    private BigDecimal minimumPurchaseAmount;
    private Integer maxPoints;
    private Boolean active;
    private Long businessId;
}