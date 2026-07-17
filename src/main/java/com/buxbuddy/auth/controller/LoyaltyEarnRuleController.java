package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleResponse;
import com.buxbuddy.auth.service.LoyaltyEarnRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loyalty/rules")
@RequiredArgsConstructor
public class LoyaltyEarnRuleController {

    private final LoyaltyEarnRuleService service;
    @PostMapping
    public ResponseEntity<LoyaltyEarnRuleResponse> create(
            @RequestBody LoyaltyEarnRuleRequest request){
        return ResponseEntity.ok(
                service.createRule(request)
        );
    }

}