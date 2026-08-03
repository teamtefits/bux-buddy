package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationRequest;
import com.buxbuddy.auth.dto.sales.DailyPaymentReconciliationResponse;
import com.buxbuddy.auth.dto.sales.MonthlyPaymentReconciliationResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.DailyPaymentReconciliation;
import com.buxbuddy.auth.enums.ReconciliationStatus;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.DailyPaymentReconciliationRepository;
import com.buxbuddy.auth.service.DailyPaymentReconciliationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyPaymentReconciliationServiceImpl
        implements DailyPaymentReconciliationService {

    private final DailyPaymentReconciliationRepository reconciliationRepository;
    private final BusinessRepository businessRepository;


    // ============================================================
    // CREATE
    // ============================================================

    @Override
    public DailyPaymentReconciliationResponse create(
            DailyPaymentReconciliationRequest request) {

        log.info(
                "Creating payment reconciliation. businessId={}, date={}",
                request.getBusinessId(),
                request.getReconciliationDate()
        );

        Business business = businessRepository
                .findById(request.getBusinessId())
                .orElseThrow(() -> {

                    log.error(
                            "Business not found. businessId={}",
                            request.getBusinessId()
                    );

                    return new RuntimeException("Business not found");
                });

        boolean exists = reconciliationRepository
                .findByBusinessIdAndReconciliationDate(
                        request.getBusinessId(),
                        request.getReconciliationDate()
                )
                .isPresent();

        if (exists) {

            log.warn(
                    "Payment reconciliation already exists. businessId={}, date={}",
                    request.getBusinessId(),
                    request.getReconciliationDate()
            );

            throw new RuntimeException(
                    "Reconciliation already exists for this business and date"
            );
        }

        DailyPaymentReconciliation reconciliation =
                new DailyPaymentReconciliation();

        reconciliation.setBusiness(business);

        reconciliation.setReconciliationDate(
                request.getReconciliationDate()
        );

        reconciliation.setInvoiceAmount(
                defaultValue(request.getInvoiceAmount())
        );

        reconciliation.setBatchAmount(
                defaultValue(request.getBatchAmount())
        );

        reconciliation.setCashAmount(
                defaultValue(request.getCashAmount())
        );

        reconciliation.setInteracAmount(
                defaultValue(request.getInteracAmount())
        );

        calculateReconciliation(reconciliation);

        DailyPaymentReconciliation saved =
                reconciliationRepository.save(reconciliation);

        log.info(
                "Payment reconciliation created successfully. id={}, businessId={}, date={}, status={}, difference={}",
                saved.getId(),
                request.getBusinessId(),
                request.getReconciliationDate(),
                saved.getStatus(),
                saved.getDifferenceAmount()
        );

        return mapToResponse(saved);
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public DailyPaymentReconciliationResponse getById(Long id) {

        log.debug(
                "Fetching payment reconciliation by id={}",
                id
        );

        DailyPaymentReconciliation reconciliation =
                reconciliationRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment reconciliation not found. id={}",
                                    id
                            );

                            return new RuntimeException(
                                    "Daily payment reconciliation not found"
                            );
                        });

        log.debug(
                "Payment reconciliation found. id={}, businessId={}, date={}",
                id,
                reconciliation.getBusiness().getId(),
                reconciliation.getReconciliationDate()
        );

        return mapToResponse(reconciliation);
    }


    // ============================================================
    // GET BY BUSINESS AND DATE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public DailyPaymentReconciliationResponse getByBusinessAndDate(
            Long businessId,
            LocalDate date) {

        log.debug(
                "Fetching payment reconciliation. businessId={}, date={}",
                businessId,
                date
        );

        DailyPaymentReconciliation reconciliation =
                reconciliationRepository
                        .findByBusinessIdAndReconciliationDate(
                                businessId,
                                date
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment reconciliation not found. businessId={}, date={}",
                                    businessId,
                                    date
                            );

                            return new RuntimeException(
                                    "Reconciliation not found for the given date"
                            );
                        });

        return mapToResponse(reconciliation);
    }


    // ============================================================
    // GET ALL BY BUSINESS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<DailyPaymentReconciliationResponse> getByBusiness(
            Long businessId) {

        log.debug(
                "Fetching all payment reconciliations. businessId={}",
                businessId
        );

        List<DailyPaymentReconciliationResponse> response =
                reconciliationRepository
                        .findByBusinessIdOrderByReconciliationDateDesc(
                                businessId
                        )
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Payment reconciliations fetched. businessId={}, count={}",
                businessId,
                response.size()
        );

        return response;
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @Override
    public DailyPaymentReconciliationResponse update(
            Long id,
            DailyPaymentReconciliationRequest request) {

        log.info(
                "Updating payment reconciliation. id={}, businessId={}, date={}",
                id,
                request.getBusinessId(),
                request.getReconciliationDate()
        );

        DailyPaymentReconciliation reconciliation =
                reconciliationRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment reconciliation not found for update. id={}",
                                    id
                            );

                            return new RuntimeException(
                                    "Daily payment reconciliation not found"
                            );
                        });

        reconciliation.setReconciliationDate(
                request.getReconciliationDate()
        );

        // IMPORTANT: invoiceAmount, not interacAmount
        reconciliation.setInvoiceAmount(
                defaultValue(request.getInvoiceAmount())
        );

        reconciliation.setBatchAmount(
                defaultValue(request.getBatchAmount())
        );

        reconciliation.setCashAmount(
                defaultValue(request.getCashAmount())
        );

        reconciliation.setInteracAmount(
                defaultValue(request.getInteracAmount())
        );

        calculateReconciliation(reconciliation);

        DailyPaymentReconciliation updated =
                reconciliationRepository.save(reconciliation);

        log.info(
                "Payment reconciliation updated successfully. id={}, status={}, difference={}",
                updated.getId(),
                updated.getStatus(),
                updated.getDifferenceAmount()
        );

        return mapToResponse(updated);
    }


    // ============================================================
    // DELETE
    // ============================================================

    @Override
    public void delete(Long id) {

        log.info(
                "Deleting payment reconciliation. id={}",
                id
        );

        if (!reconciliationRepository.existsById(id)) {

            log.warn(
                    "Cannot delete payment reconciliation because it was not found. id={}",
                    id
            );

            throw new RuntimeException(
                    "Daily payment reconciliation not found"
            );
        }

        reconciliationRepository.deleteById(id);

        log.info(
                "Payment reconciliation deleted successfully. id={}",
                id
        );
    }


    // ============================================================
    // CALCULATE RECONCILIATION
    // ============================================================

    private void calculateReconciliation(
            DailyPaymentReconciliation reconciliation) {

        BigDecimal paymentTotal =
                reconciliation.getBatchAmount()
                        .add(reconciliation.getCashAmount())
                        .add(reconciliation.getInteracAmount());

        BigDecimal invoiceAmount =
                reconciliation.getInvoiceAmount();

        BigDecimal difference =
                invoiceAmount.subtract(paymentTotal);

        reconciliation.setDifferenceAmount(difference);

        if (difference.compareTo(BigDecimal.ZERO) == 0) {

            reconciliation.setStatus(
                    ReconciliationStatus.BALANCED
            );

        } else if (difference.compareTo(BigDecimal.ZERO) > 0) {

            reconciliation.setStatus(
                    ReconciliationStatus.SHORT
            );

        } else {

            reconciliation.setStatus(
                    ReconciliationStatus.OVER
            );
        }

        log.debug(
                "Reconciliation calculated. invoice={}, batch={}, cash={}, interac={}, paymentTotal={}, difference={}, status={}",
                invoiceAmount,
                reconciliation.getBatchAmount(),
                reconciliation.getCashAmount(),
                reconciliation.getInteracAmount(),
                paymentTotal,
                difference,
                reconciliation.getStatus()
        );
    }


    // ============================================================
    // GET BY DATE RANGE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<DailyPaymentReconciliationResponse> getByBusinessAndDateRange(
            Long businessId,
            LocalDate startDate,
            LocalDate endDate) {

        log.info(
                "Fetching payment reconciliations by date range. businessId={}, startDate={}, endDate={}",
                businessId,
                startDate,
                endDate
        );

        if (startDate.isAfter(endDate)) {

            log.warn(
                    "Invalid date range. businessId={}, startDate={}, endDate={}",
                    businessId,
                    startDate,
                    endDate
            );

            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        List<DailyPaymentReconciliationResponse> response =
                reconciliationRepository
                        .findByBusinessIdAndReconciliationDateBetweenOrderByReconciliationDateAsc(
                                businessId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Date range reconciliation fetched. businessId={}, startDate={}, endDate={}, count={}",
                businessId,
                startDate,
                endDate,
                response.size()
        );

        return response;
    }


    // ============================================================
    // GET BY MONTH
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<DailyPaymentReconciliationResponse> getByBusinessAndMonth(
            Long businessId,
            int year,
            int month) {

        log.info(
                "Fetching monthly daily reconciliations. businessId={}, year={}, month={}",
                businessId,
                year,
                month
        );

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DailyPaymentReconciliationResponse> response =
                reconciliationRepository
                        .findByBusinessIdAndReconciliationDateBetweenOrderByReconciliationDateAsc(
                                businessId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Monthly daily reconciliations fetched. businessId={}, year={}, month={}, count={}",
                businessId,
                year,
                month,
                response.size()
        );

        return response;
    }


    // ============================================================
    // MONTHLY SUMMARY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public MonthlyPaymentReconciliationResponse getMonthlyReconciliation(
            Long businessId,
            int year,
            int month) {

        log.info(
                "Generating monthly payment reconciliation report. businessId={}, year={}, month={}",
                businessId,
                year,
                month
        );

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DailyPaymentReconciliation> records =
                reconciliationRepository
                        .findByBusinessIdAndReconciliationDateBetweenOrderByReconciliationDateAsc(
                                businessId,
                                startDate,
                                endDate
                        );

        log.debug(
                "Monthly reconciliation records loaded. businessId={}, startDate={}, endDate={}, recordCount={}",
                businessId,
                startDate,
                endDate,
                records.size()
        );

        BigDecimal totalInvoiceAmount = records.stream()
                .map(DailyPaymentReconciliation::getInvoiceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBatchAmount = records.stream()
                .map(DailyPaymentReconciliation::getBatchAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCashAmount = records.stream()
                .map(DailyPaymentReconciliation::getCashAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInteracAmount = records.stream()
                .map(DailyPaymentReconciliation::getInteracAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaymentAmount =
                totalBatchAmount
                        .add(totalCashAmount)
                        .add(totalInteracAmount);

        BigDecimal totalDifferenceAmount =
                totalInvoiceAmount.subtract(totalPaymentAmount);

        ReconciliationStatus status;
        if (totalDifferenceAmount.compareTo(BigDecimal.ZERO) == 0) {
            status = ReconciliationStatus.BALANCED;
        } else if (totalDifferenceAmount.compareTo(BigDecimal.ZERO) > 0) {
            status = ReconciliationStatus.SHORT;

        } else {
            status = ReconciliationStatus.OVER;
        }
        log.info(
                "Monthly reconciliation calculated. businessId={}, year={}, month={}, invoiceTotal={}, batchTotal={}, cashTotal={}, interacTotal={}, paymentTotal={}, difference={}, status={}",
                businessId,
                year,
                month,
                totalInvoiceAmount,
                totalBatchAmount,
                totalCashAmount,
                totalInteracAmount,
                totalPaymentAmount,
                totalDifferenceAmount,
                status
        );

        List<DailyPaymentReconciliationResponse> dailyRecords =
                records.stream()
                        .map(this::mapToResponse)
                        .toList();

        MonthlyPaymentReconciliationResponse response =
                MonthlyPaymentReconciliationResponse.builder()
                        .businessId(businessId)
                        .year(year)
                        .month(month)
                        .startDate(startDate)
                        .endDate(endDate)
                        .totalInvoiceAmount(totalInvoiceAmount)
                        .totalBatchAmount(totalBatchAmount)
                        .totalCashAmount(totalCashAmount)
                        .totalInteracAmount(totalInteracAmount)
                        .totalPaymentAmount(totalPaymentAmount)
                        .totalDifferenceAmount(totalDifferenceAmount)
                        .status(status)
                        .dailyRecords(dailyRecords)
                        .build();

        log.info(
                "Monthly payment reconciliation report generated successfully. businessId={}, year={}, month={}, status={}",
                businessId,
                year,
                month,
                status
        );

        return response;
    }

    // ============================================================
    // DEFAULT VALUE
    // ============================================================

    private BigDecimal defaultValue(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    // ============================================================
    // MAP ENTITY TO RESPONSE
    // ============================================================

    private DailyPaymentReconciliationResponse mapToResponse(
            DailyPaymentReconciliation entity) {

        return DailyPaymentReconciliationResponse.builder()
                .id(entity.getId())
                .businessId(entity.getBusiness().getId())
                .reconciliationDate(entity.getReconciliationDate())
                .invoiceAmount(entity.getInvoiceAmount())
                .batchAmount(entity.getBatchAmount())
                .cashAmount(entity.getCashAmount())
                .interacAmount(entity.getInteracAmount())
                .differenceAmount(entity.getDifferenceAmount())
                .status(entity.getStatus())
                .build();
    }
}