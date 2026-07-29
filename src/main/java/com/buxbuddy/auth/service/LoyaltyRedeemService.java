package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.Loyalty.redeem.LoyaltyRedeemRequest;
import com.buxbuddy.auth.dto.Loyalty.redeem.LoyaltyRedeemResponse;

public interface LoyaltyRedeemService {
    public LoyaltyRedeemResponse redeemPoints(
            LoyaltyRedeemRequest request);
}
