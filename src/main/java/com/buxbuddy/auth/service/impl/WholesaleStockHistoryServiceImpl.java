package com.buxbuddy.auth.service.impl;


import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockHistoryRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockHistoryResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.WholesaleProduct;
import com.buxbuddy.auth.entity.WholesaleStockHistory;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.WholesaleProductRepository;
import com.buxbuddy.auth.repository.WholesaleStockHistoryRepository;
import com.buxbuddy.auth.service.WholesaleStockHistoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WholesaleStockHistoryServiceImpl
        implements WholesaleStockHistoryService {

    private final WholesaleStockHistoryRepository historyRepository;

    private final BusinessRepository businessRepository;

    private final WholesaleProductRepository wholesaleProductRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public WholesaleStockHistoryResponse create(
            Long businessId,
            WholesaleStockHistoryRequest request
    ) {

        validateRequest(request);

        if (historyRepository
                .existsByBusinessIdAndWholesaleProductIdAndStockDate(
                        businessId,
                        request.getWholesaleProductId(),
                        request.getStockDate()
                )) {

            throw new IllegalArgumentException(
                    "Stock history already exists for this product and date"
            );
        }

        Business business =
                businessRepository.findById(businessId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Business not found"
                                )
                        );

        WholesaleProduct wholesaleProduct =
                wholesaleProductRepository
                        .findById(request.getWholesaleProductId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Wholesale product not found"
                                )
                        );

        WholesaleStockHistory history =
                WholesaleStockHistory.builder()
                        .business(business)
                        .wholesaleProduct(wholesaleProduct)
                        .stockDate(request.getStockDate())
                        .openingStock(
                                defaultZero(request.getOpeningStock())
                        )
                        .stockAdded(
                                defaultZero(request.getStockAdded())
                        )
                        .totalSales(
                                defaultZero(request.getTotalSales())
                        )
                        .remainingStock(
                                calculateRemainingStock(request)
                        )
                        .build();

        history = historyRepository.save(history);

        return mapToResponse(history);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public WholesaleStockHistoryResponse getById(
            Long businessId,
            Long historyId
    ) {

        WholesaleStockHistory history =
                historyRepository.findById(historyId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Wholesale stock history not found"
                                )
                        );

        validateBusiness(history, businessId);

        return mapToResponse(history);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleStockHistoryResponse> getAll(
            Long businessId
    ) {

        return historyRepository
                .findByBusinessIdOrderByStockDateDesc(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET BY PRODUCT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleStockHistoryResponse> getByProduct(
            Long businessId,
            Long wholesaleProductId
    ) {

        return historyRepository
                .findByBusinessIdAndWholesaleProductIdOrderByStockDateDesc(
                        businessId,
                        wholesaleProductId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET BY PRODUCT + DATE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public WholesaleStockHistoryResponse getByProductAndDate(
            Long businessId,
            Long wholesaleProductId,
            LocalDate stockDate
    ) {

        WholesaleStockHistory history =
                historyRepository
                        .findByBusinessIdAndWholesaleProductIdAndStockDate(
                                businessId,
                                wholesaleProductId,
                                stockDate
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Stock history not found for product and date"
                                )
                        );

        return mapToResponse(history);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public WholesaleStockHistoryResponse update(
            Long businessId,
            Long historyId,
            WholesaleStockHistoryRequest request
    ) {

        validateRequest(request);

        WholesaleStockHistory history =
                historyRepository.findById(historyId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Wholesale stock history not found"
                                )
                        );

        validateBusiness(history, businessId);

        /*
         * If product or date is changing,
         * make sure another history record does not already exist.
         */

        boolean productChanged =
                !history.getWholesaleProduct()
                        .getId()
                        .equals(request.getWholesaleProductId());

        boolean dateChanged =
                !history.getStockDate()
                        .equals(request.getStockDate());

        if (productChanged || dateChanged) {

            boolean exists =
                    historyRepository
                            .existsByBusinessIdAndWholesaleProductIdAndStockDate(
                                    businessId,
                                    request.getWholesaleProductId(),
                                    request.getStockDate()
                            );

            if (exists) {

                throw new IllegalArgumentException(
                        "Stock history already exists for this product and date"
                );
            }

            WholesaleProduct wholesaleProduct =
                    wholesaleProductRepository
                            .findById(request.getWholesaleProductId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Wholesale product not found"
                                    )
                            );

            history.setWholesaleProduct(wholesaleProduct);
            history.setStockDate(request.getStockDate());
        }

        history.setOpeningStock(
                defaultZero(request.getOpeningStock())
        );

        history.setStockAdded(
                defaultZero(request.getStockAdded())
        );

        history.setTotalSales(
                defaultZero(request.getTotalSales())
        );

        history.setRemainingStock(
                calculateRemainingStock(request)
        );

        history =
                historyRepository.save(history);

        return mapToResponse(history);
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(
            Long businessId,
            Long historyId
    ) {

        WholesaleStockHistory history =
                historyRepository.findById(historyId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Wholesale stock history not found"
                                )
                        );

        validateBusiness(history, businessId);

        historyRepository.delete(history);
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateRequest(
            WholesaleStockHistoryRequest request
    ) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Request cannot be null"
            );
        }

        if (request.getWholesaleProductId() == null) {

            throw new IllegalArgumentException(
                    "Wholesale product ID is required"
            );
        }

        if (request.getStockDate() == null) {

            throw new IllegalArgumentException(
                    "Stock date is required"
            );
        }

        validateNonNegative(
                request.getOpeningStock(),
                "Opening stock"
        );

        validateNonNegative(
                request.getStockAdded(),
                "Stock added"
        );

        validateNonNegative(
                request.getTotalSales(),
                "Total sales"
        );

        validateNonNegative(
                request.getRemainingStock(),
                "Remaining stock"
        );
    }


    private void validateNonNegative(
            BigDecimal value,
            String fieldName
    ) {

        if (value != null &&
                value.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    fieldName + " cannot be negative"
            );
        }
    }


    private BigDecimal defaultZero(
            BigDecimal value
    ) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    // =========================================================
    // CALCULATE REMAINING STOCK
    // =========================================================

    private BigDecimal calculateRemainingStock(
            WholesaleStockHistoryRequest request
    ) {

        BigDecimal opening =
                defaultZero(request.getOpeningStock());

        BigDecimal added =
                defaultZero(request.getStockAdded());

        BigDecimal sales =
                defaultZero(request.getTotalSales());

        BigDecimal calculated =
                opening
                        .add(added)
                        .subtract(sales);

        if (calculated.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Total sales cannot be greater than available stock"
            );
        }

        return calculated;
    }


    // =========================================================
    // BUSINESS VALIDATION
    // =========================================================

    private void validateBusiness(
            WholesaleStockHistory history,
            Long businessId
    ) {

        if (!history.getBusiness()
                .getId()
                .equals(businessId)) {

            throw new IllegalArgumentException(
                    "Stock history does not belong to this business"
            );
        }
    }


    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    private WholesaleStockHistoryResponse mapToResponse(
            WholesaleStockHistory history
    ) {

        return WholesaleStockHistoryResponse.builder()

                .id(history.getId())

                .businessId(
                        history.getBusiness().getId()
                )

                .wholesaleProductId(
                        history.getWholesaleProduct().getId()
                )

                .productName(
                        history.getWholesaleProduct().getProductName()
                )

                .stockDate(
                        history.getStockDate()
                )

                .openingStock(
                        history.getOpeningStock()
                )

                .stockAdded(
                        history.getStockAdded()
                )

                .totalSales(
                        history.getTotalSales()
                )

                .remainingStock(
                        history.getRemainingStock()
                )

                .createdAt(
                        history.getCreatedAt()
                )

                .updatedAt(
                        history.getUpdatedAt()
                )

                .build();
    }
}
