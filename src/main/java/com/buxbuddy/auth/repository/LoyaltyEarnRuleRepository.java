package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.LoyaltyEarnRule;
import com.buxbuddy.auth.enums.LoyaltyRuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoyaltyEarnRuleRepository extends JpaRepository<LoyaltyEarnRule, Long> {

    List<LoyaltyEarnRule> findByBusinessAndActiveTrue(Business business);

    List<LoyaltyEarnRule> findByBusiness(Business business);
    List<LoyaltyEarnRule> findByBusinessIdAndActiveTrue(Long businessId);
    List<LoyaltyEarnRule> findByBusiness_IdAndActiveTrue(Long businessId);
    Optional<LoyaltyEarnRule> findByBusiness_IdAndRuleTypeAndActiveTrue(Long businessId, LoyaltyRuleType ruleType
    );
}

