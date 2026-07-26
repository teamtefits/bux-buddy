package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleResponse;
import com.buxbuddy.auth.service.LoyaltyEarnRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<LoyaltyEarnRuleResponse>> getAllByBusiness(
            @PathVariable Long businessId) {
        return ResponseEntity.ok(service.getAllRules(businessId));
    }

    @GetMapping("/{ruleId}/business/{businessId}")
    public ResponseEntity<LoyaltyEarnRuleResponse> getById(
            @PathVariable Long ruleId,
            @PathVariable Long businessId) {
        return ResponseEntity.ok(service.getRule(ruleId, businessId));
    }

    @PutMapping("/{ruleId}/business/{businessId}")
    public ResponseEntity<LoyaltyEarnRuleResponse> update(
            @PathVariable Long ruleId,
            @PathVariable Long businessId,
            @RequestBody LoyaltyEarnRuleRequest request) {
        return ResponseEntity.ok(service.updateRule(ruleId, businessId, request));
    }

    @DeleteMapping("/{ruleId}/business/{businessId}")
    public ResponseEntity<String> delete(
            @PathVariable Long ruleId,
            @PathVariable Long businessId) {
        service.deleteRule(ruleId, businessId);
        return ResponseEntity.ok("Rule deleted successfully.");
    }

}