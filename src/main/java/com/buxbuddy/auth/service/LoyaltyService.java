package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.Loyalty.customer.CustomerLoyaltyResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleResponse;

public interface  LoyaltyService {
    CustomerLoyaltyResponse getCustomerByPhone(String phone);
    LoyaltyEarnResponse earnPoints(LoyaltyEarnRequest request);
    LoyaltyRedeemRuleResponse createRule(LoyaltyRedeemRuleRequest request
    );
}
