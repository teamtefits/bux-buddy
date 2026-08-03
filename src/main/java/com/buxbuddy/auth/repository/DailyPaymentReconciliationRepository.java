package com.buxbuddy.auth.repository;


import com.buxbuddy.auth.entity.DailyPaymentReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyPaymentReconciliationRepository
        extends JpaRepository<DailyPaymentReconciliation, Long> {

    Optional<DailyPaymentReconciliation> findByBusinessIdAndReconciliationDate(
            Long businessId,
            LocalDate reconciliationDate
    );

    List<DailyPaymentReconciliation> findByBusinessIdOrderByReconciliationDateDesc(
            Long businessId
    );
    List<DailyPaymentReconciliation>
    findByBusinessIdAndReconciliationDateBetweenOrderByReconciliationDateAsc(
            Long businessId,
            LocalDate startDate,
            LocalDate endDate
    );


}