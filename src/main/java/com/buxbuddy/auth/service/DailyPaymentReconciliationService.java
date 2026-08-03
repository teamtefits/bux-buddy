package com.buxbuddy.auth.service;



import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationRequest;
import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationResponse;
import com.buxbuddy.auth.dto.sales.MonthlyPaymentReconciliationResponse;

import java.time.LocalDate;
import java.util.List;

public interface DailyPaymentReconciliationService {
    DailyPaymentReconciliationResponse create(
            DailyPaymentReconciliationRequest request
    );
    DailyPaymentReconciliationResponse getById(Long id);
    DailyPaymentReconciliationResponse getByBusinessAndDate(
            Long businessId,
            LocalDate date
    );
    List<DailyPaymentReconciliationResponse> getByBusiness(
            Long businessId
    );
    DailyPaymentReconciliationResponse update(
            Long id,
            DailyPaymentReconciliationRequest request
    );
    void delete(Long id);
    List<DailyPaymentReconciliationResponse> getByBusinessAndDateRange(
            Long businessId,
            LocalDate startDate,
            LocalDate endDate
    );
    List<DailyPaymentReconciliationResponse> getByBusinessAndMonth(
            Long businessId,
            int year,
            int month
    );
    MonthlyPaymentReconciliationResponse getMonthlyReconciliation(
            Long businessId,
            int year,
            int month
    );
}