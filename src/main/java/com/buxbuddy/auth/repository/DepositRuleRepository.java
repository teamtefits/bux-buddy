package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.DepositRule;
import com.buxbuddy.auth.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositRuleRepository
        extends JpaRepository<DepositRule, Long> {

    // Existing method - keep this
    List<DepositRule> findByProvinceAndActiveTrue(
            Province province
    );

    // Optimized method
    @Query("""
        SELECT dr
        FROM DepositRule dr
        WHERE dr.province.id = :provinceId
        AND dr.active = true
        """)
    List<DepositRule> findActiveRulesByProvinceId(
            @Param("provinceId") Long provinceId
    );
}