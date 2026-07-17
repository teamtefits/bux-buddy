package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleResponse;
import java.util.List;

public interface LoyaltyEarnRuleService {

    LoyaltyEarnRuleResponse createRule(
            LoyaltyEarnRuleRequest request
    );
    List<LoyaltyEarnRuleResponse> getRulesByBusiness(
            Long businessId
    );
    LoyaltyEarnRuleResponse getRuleById(
            Long id
    );
    LoyaltyEarnRuleResponse updateRule(
            Long id,
            LoyaltyEarnRuleRequest request
    );
    void deleteRule(Long id);
}
