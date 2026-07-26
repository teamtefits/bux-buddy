package com.buxbuddy.auth.dto.Loyalty.rule;

import com.buxbuddy.auth.enums.LoyaltyRuleType;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyEarnRuleRequest {

    private String name;
    private LoyaltyRuleType ruleType;
    /*
       Cashback percentage
       Example:
       2 = 2%
    */
    private BigDecimal cashbackPercentage;
    /*
       Extra bonus
       Example:
       Birthday extra 5%
    */
    private BigDecimal bonusPercentage;
    private DayOfWeek dayOfWeek;
    private Integer birthdayMonth;

    /*
       Minimum purchase required
       Example:
       Spend $50 minimum
    */
    private BigDecimal minimumPurchaseAmount;
    private Integer maxPoints;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
    private Long businessId;
}