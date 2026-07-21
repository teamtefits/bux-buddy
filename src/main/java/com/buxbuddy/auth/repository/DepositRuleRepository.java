package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.DepositRule;
import com.buxbuddy.auth.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRuleRepository
        extends JpaRepository<DepositRule, Long> {

    List<DepositRule> findByProvinceAndActiveTrue(
            Province province
    );
}