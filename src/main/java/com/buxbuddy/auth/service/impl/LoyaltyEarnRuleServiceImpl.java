package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.rule.LoyaltyEarnRuleResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.LoyaltyEarnRule;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.LoyaltyEarnRuleRepository;
import com.buxbuddy.auth.service.LoyaltyEarnRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoyaltyEarnRuleServiceImpl implements LoyaltyEarnRuleService {
    private final LoyaltyEarnRuleRepository repository;
    private final BusinessRepository businessRepository;
    @Override
    public LoyaltyEarnRuleResponse createRule(
            LoyaltyEarnRuleRequest request) {
        Business business =
                businessRepository.findById(request.getBusinessId())
                        .orElseThrow(() ->
                                new RuntimeException("Business not found"));

        LoyaltyEarnRule rule =
                LoyaltyEarnRule.builder()
                        .name(request.getName())
                        .ruleType(request.getRuleType())
                        .cashbackPercentage(request.getCashbackPercentage())
                        .bonusPercentage(request.getBonusPercentage())
                        .dayOfWeek(request.getDayOfWeek())
                        .birthdayMonth(request.getBirthdayMonth())
                        .minimumPurchaseAmount(
                                request.getMinimumPurchaseAmount()
                        )
                        .maxPoints(request.getMaxPoints())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .active(
                                request.getActive()==null
                                        ? true
                                        : request.getActive()
                        )
                        .business(business)
                        .build();
        System.out.println("Entity cashbackPercentage = "
                + rule.getCashbackPercentage());
        System.out.println("Entity minimumPurchaseAmount = "
                + rule.getMinimumPurchaseAmount());
        LoyaltyEarnRule saved =
                repository.save(rule);
        return mapToResponse(saved);
    }

    @Override
    public List<LoyaltyEarnRuleResponse> getRulesByBusiness(Long businessId) {
        return List.of();
    }

    @Override
    public List<LoyaltyEarnRuleResponse> getAllRules(Long businessId) {

        return repository.findByBusinessIdOrderByRuleType(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LoyaltyEarnRuleResponse getRuleById(Long id) {
        return null;
    }

    @Override
    public LoyaltyEarnRuleResponse updateRule(Long id, LoyaltyEarnRuleRequest request) {
        return null;
    }

    @Override
    public LoyaltyEarnRuleResponse getRule(Long ruleId, Long businessId) {
        LoyaltyEarnRule rule = repository.findByIdAndBusinessId(ruleId, businessId)
                .orElseThrow(() -> new RuntimeException("Rule not found"));
        return mapToResponse(rule);
    }

    @Override
    public LoyaltyEarnRuleResponse updateRule(Long ruleId,
                                              Long businessId,
                                              LoyaltyEarnRuleRequest request) {

        LoyaltyEarnRule rule = repository.findByIdAndBusinessId(ruleId, businessId)
                .orElseThrow(() -> new RuntimeException("Rule not found"));
        rule.setName(request.getName());
        rule.setRuleType(request.getRuleType());
        rule.setCashbackPercentage(request.getCashbackPercentage());
        rule.setBonusPercentage(request.getBonusPercentage());
        rule.setDayOfWeek(request.getDayOfWeek());
        rule.setBirthdayMonth(request.getBirthdayMonth());
        rule.setMinimumPurchaseAmount(request.getMinimumPurchaseAmount());
        rule.setMaxPoints(request.getMaxPoints());
        rule.setStartDate(request.getStartDate());
        rule.setEndDate(request.getEndDate());
        rule.setActive(request.getActive());
        repository.save(rule);
        return mapToResponse(rule);
    }

    @Override
    public void deleteRule(Long ruleId, Long businessId) {

        LoyaltyEarnRule rule = repository.findByIdAndBusinessId(ruleId, businessId)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        repository.delete(rule);
    }

    private LoyaltyEarnRuleResponse mapToResponse(
            LoyaltyEarnRule rule){
        return LoyaltyEarnRuleResponse.builder()
                .id(rule.getId())
                .name(rule.getName())
                .ruleType(rule.getRuleType())
                .multiplier(rule.getCashbackPercentage())
                .bonusPercentage(rule.getBonusPercentage())
                .minimumPurchaseAmount(
                        rule.getMinimumPurchaseAmount()
                )
                .maxPoints(rule.getMaxPoints())
                .active(rule.getActive())
                .businessId(
                        rule.getBusiness().getId()
                )
                .build();
    }
}