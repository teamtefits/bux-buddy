package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleResponse;
import com.buxbuddy.auth.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loyalty/redeem-rules")
@RequiredArgsConstructor
public class LoyaltyRedeemRuleController {


    private final LoyaltyService loyaltyService;


    @PostMapping
    public ResponseEntity<LoyaltyRedeemRuleResponse> create(
            @RequestBody LoyaltyRedeemRuleRequest request) {
        return ResponseEntity.ok(
                loyaltyService.createRule(request)
        );
    }
}