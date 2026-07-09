package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.StockHistory;
import com.buxbuddy.auth.enums.StockAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    List<StockHistory> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<StockHistory> findByBusinessIdOrderByCreatedAtDesc(Long businessId);

    List<StockHistory> findByBusinessIdAndActionOrderByCreatedAtDesc(
            Long businessId,
            StockAction action);

    List<StockHistory> findByBusinessIdAndCreatedAtBetween(
            Long businessId,
            LocalDateTime start,
            LocalDateTime end);

    List<StockHistory> findByProductIdAndCreatedAtBetween(
            Long productId,
            LocalDateTime start,
            LocalDateTime end);
}