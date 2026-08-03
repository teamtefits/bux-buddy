package com.buxbuddy.auth.service.impl;
import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.WholesaleProduct;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.WholesaleProductRepository;
import com.buxbuddy.auth.service.WholesaleProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WholesaleProductServiceImpl
        implements WholesaleProductService {

    private final WholesaleProductRepository wholesaleProductRepository;
    private final BusinessRepository businessRepository;

    @Override
    public WholesaleProductResponse create(
            WholesaleProductRequest request) {

        log.info(
                "Creating wholesale product. businessId={}, productName={}",
                request.getBusinessId(),
                request.getProductName()
        );

        Business business =
                businessRepository.findById(request.getBusinessId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Business not found"
                                )
                        );

        boolean exists =
                wholesaleProductRepository
                        .existsByBusinessIdAndProductName(
                                request.getBusinessId(),
                                request.getProductName()
                        );

        if (exists) {
            throw new RuntimeException(
                    "Wholesale product already exists for this business"
            );
        }

        WholesaleProduct entity =
                WholesaleProduct.builder()
                        .business(business)
                        .productName(request.getProductName())
                        .costPrice(
                                defaultValue(request.getCostPrice())
                        )
                        .wholesalePrice(
                                defaultValue(request.getWholesalePrice())
                        )
                        .active(
                                request.getActive() == null
                                        ? true
                                        : request.getActive()
                        )
                        .build();

        WholesaleProduct saved =
                wholesaleProductRepository.save(entity);

        log.info(
                "Wholesale product created successfully. id={}",
                saved.getId()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleProductResponse getById(Long id) {

        WholesaleProduct entity =
                wholesaleProductRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale product not found"
                                )
                        );

        return mapToResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleProductResponse> getByBusiness(
            Long businessId) {

        return wholesaleProductRepository
                .findByBusinessIdAndActiveTrue(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public WholesaleProductResponse update(
            Long id,
            WholesaleProductRequest request) {

        WholesaleProduct entity =
                wholesaleProductRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale product not found"
                                )
                        );

        if (request.getProductName() != null) {
            entity.setProductName(
                    request.getProductName()
            );
        }

        if (request.getCostPrice() != null) {
            entity.setCostPrice(
                    request.getCostPrice()
            );
        }

        if (request.getWholesalePrice() != null) {
            entity.setWholesalePrice(
                    request.getWholesalePrice()
            );
        }

        if (request.getActive() != null) {
            entity.setActive(
                    request.getActive()
            );
        }

        WholesaleProduct updated =
                wholesaleProductRepository.save(entity);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        WholesaleProduct entity =
                wholesaleProductRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale product not found"
                                )
                        );

        // Soft delete
        entity.setActive(false);

        wholesaleProductRepository.save(entity);

        log.info(
                "Wholesale product deactivated. id={}",
                id
        );
    }

    private BigDecimal defaultValue(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    private WholesaleProductResponse mapToResponse(
            WholesaleProduct entity) {

        return WholesaleProductResponse.builder()
                .id(entity.getId())
                .businessId(
                        entity.getBusiness().getId()
                )
                .productName(
                        entity.getProductName()
                )
                .costPrice(
                        entity.getCostPrice()
                )
                .wholesalePrice(
                        entity.getWholesalePrice()
                )
                .active(
                        entity.getActive()
                )
                .createdAt(
                        entity.getCreatedAt()
                )
                .updatedAt(
                        entity.getUpdatedAt()
                )
                .build();
    }
}