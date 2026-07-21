package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRuleRepository
        extends JpaRepository<TaxRule, Long> {
    List<TaxRule> findByProvinceAndActiveTrue(
            Province province
    );

}
