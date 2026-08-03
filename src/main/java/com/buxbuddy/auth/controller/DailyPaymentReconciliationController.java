package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationRequest;
import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationResponse;
import com.buxbuddy.auth.dto.sales.MonthlyPaymentReconciliationResponse;
import com.buxbuddy.auth.service.DailyPaymentReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reconciliations")
@RequiredArgsConstructor
public class DailyPaymentReconciliationController {

    private final DailyPaymentReconciliationService reconciliationService;

    @PostMapping
    public ResponseEntity<DailyPaymentReconciliationResponse> create(
            @RequestBody DailyPaymentReconciliationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reconciliationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyPaymentReconciliationResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                reconciliationService.getById(id));
    }

    @GetMapping("/business/{businessId}/date/{date}")
    public ResponseEntity<DailyPaymentReconciliationResponse>
    getByBusinessAndDate(
            @PathVariable Long businessId,
            @PathVariable LocalDate date) {
        return ResponseEntity.ok(
                reconciliationService.getByBusinessAndDate(
                        businessId,
                        date));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<DailyPaymentReconciliationResponse>>
    getByBusiness(
            @PathVariable Long businessId) {
        return ResponseEntity.ok(
                reconciliationService.getByBusiness(businessId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyPaymentReconciliationResponse> update(
            @PathVariable Long id,
            @RequestBody DailyPaymentReconciliationRequest request) {
        return ResponseEntity.ok(
                reconciliationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        reconciliationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/business/{businessId}/date-range")
    public ResponseEntity<List<DailyPaymentReconciliationResponse>>
    getByBusinessAndDateRange(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                reconciliationService.getByBusinessAndDateRange(
                        businessId,
                        startDate,
                        endDate
                )
        );
    }
    @GetMapping("/business/{businessId}/month")
    public ResponseEntity<List<DailyPaymentReconciliationResponse>>
    getByBusinessAndMonth(
            @PathVariable Long businessId,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                reconciliationService.getByBusinessAndMonth(
                        businessId,
                        year,
                        month
                )
        );
    }

    @GetMapping("/business/{businessId}/monthly")
    public ResponseEntity<MonthlyPaymentReconciliationResponse>
    getMonthlyReconciliation(
            @PathVariable Long businessId,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                reconciliationService.getMonthlyReconciliation(
                        businessId,
                        year,
                        month
                )
        );
    }
}