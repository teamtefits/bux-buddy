package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.LoyaltyRedeemRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyRedeemRuleRepository
        extends JpaRepository<LoyaltyRedeemRule, Long> {
    Optional<LoyaltyRedeemRule> findByBusiness_IdAndActiveTrue(Long businessId);

}