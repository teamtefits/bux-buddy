package com.buxbuddy.auth.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.buxbuddy.auth.entity.WholesaleStockHistory;

import java.time.LocalDate;

import com.buxbuddy.auth.entity.WholesaleStockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WholesaleStockHistoryRepository
        extends JpaRepository<WholesaleStockHistory, Long> {

    Optional<WholesaleStockHistory>
    findByBusinessIdAndWholesaleProductIdAndStockDate(
            Long businessId,
            Long wholesaleProductId,
            LocalDate stockDate
    );

    List<WholesaleStockHistory>
    findByBusinessIdAndWholesaleProductIdOrderByStockDateDesc(
            Long businessId,
            Long wholesaleProductId
    );

    List<WholesaleStockHistory>
    findByBusinessIdOrderByStockDateDesc(
            Long businessId
    );

    boolean existsByBusinessIdAndWholesaleProductIdAndStockDate(
            Long businessId,
            Long wholesaleProductId,
            LocalDate stockDate
    );

    boolean existsByBusinessIdAndWholesaleProductId(Long businessId, @NotNull(message = "Wholesale product ID is required") Long wholesaleProductId);
}