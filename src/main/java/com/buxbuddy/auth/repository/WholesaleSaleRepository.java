package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.WholesaleSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleSaleRepository
        extends JpaRepository<WholesaleSale, Long> {

    Optional<WholesaleSale>
    findByBusinessIdAndInvoiceNumber(
            Long businessId,
            String invoiceNumber
    );

    List<WholesaleSale>
    findByBusinessIdOrderBySaleDateDesc(
            Long businessId
    );

    List<WholesaleSale>
    findByBusinessIdAndSaleDateBetweenOrderBySaleDateAsc(
            Long businessId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<WholesaleSale>
    findByBusinessIdAndShopIdOrderBySaleDateDesc(
            Long businessId,
            Long shopId
    );

    List<WholesaleSale>
    findByBusinessIdAndPartnerIdOrderBySaleDateDesc(
            Long businessId,
            Long partnerId
    );

    List<WholesaleSale>
    findByBusinessIdAndShopIdAndSaleDateBetweenOrderBySaleDateAsc(
            Long businessId,
            Long shopId,
            LocalDate startDate,
            LocalDate endDate
    );
}