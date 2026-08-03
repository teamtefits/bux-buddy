package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaxRuleRepository
        extends JpaRepository<TaxRule, Long> {

    @Query("""
        SELECT DISTINCT tr
        FROM TaxRule tr
        JOIN FETCH tr.taxType tt
        WHERE tr.province.id = :provinceId
          AND tr.active = true
        """)
    List<TaxRule> findActiveRulesByProvinceId(
            @Param("provinceId") Long provinceId
    );
}