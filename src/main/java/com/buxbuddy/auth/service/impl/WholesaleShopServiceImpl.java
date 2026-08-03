package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.WholesaleShop;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.WholesaleShopRepository;
import com.buxbuddy.auth.service.WholesaleShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WholesaleShopServiceImpl
        implements WholesaleShopService {

    private final WholesaleShopRepository wholesaleShopRepository;
    private final BusinessRepository businessRepository;

    @Override
    public WholesaleShopResponse create(
            WholesaleShopRequest request) {

        log.info(
                "Creating wholesale shop. businessId={}, shopName={}",
                request.getBusinessId(),
                request.getShopName()
        );

        Business business = businessRepository
                .findById(request.getBusinessId())
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));

        if (wholesaleShopRepository
                .existsByBusinessIdAndShopName(
                        request.getBusinessId(),
                        request.getShopName())) {

            throw new RuntimeException(
                    "Wholesale shop already exists for this business"
            );
        }

        WholesaleShop shop = WholesaleShop.builder()
                .business(business)
                .shopName(request.getShopName())
                .contactName(request.getContactName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .active(
                        request.getActive() == null
                                ? true
                                : request.getActive()
                )
                .build();

        WholesaleShop saved =
                wholesaleShopRepository.save(shop);

        log.info(
                "Wholesale shop created successfully. shopId={}",
                saved.getId()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleShopResponse getById(Long id) {

        log.info("Fetching wholesale shop. shopId={}", id);

        WholesaleShop shop =
                wholesaleShopRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale shop not found"
                                ));

        return mapToResponse(shop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleShopResponse> getByBusiness(
            Long businessId) {

        log.info(
                "Fetching wholesale shops. businessId={}",
                businessId
        );

        return wholesaleShopRepository
                .findByBusinessIdOrderByShopNameAsc(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public WholesaleShopResponse update(
            Long id,
            WholesaleShopRequest request) {

        log.info(
                "Updating wholesale shop. shopId={}",
                id
        );

        WholesaleShop shop =
                wholesaleShopRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale shop not found"
                                ));

        if (!shop.getShopName()
                .equalsIgnoreCase(request.getShopName())
                && wholesaleShopRepository
                .existsByBusinessIdAndShopName(
                        shop.getBusiness().getId(),
                        request.getShopName())) {

            throw new RuntimeException(
                    "Another wholesale shop with this name already exists"
            );
        }

        shop.setShopName(request.getShopName());
        shop.setContactName(request.getContactName());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());
        shop.setAddressLine1(request.getAddressLine1());
        shop.setAddressLine2(request.getAddressLine2());
        shop.setCity(request.getCity());
        shop.setProvince(request.getProvince());
        shop.setPostalCode(request.getPostalCode());

        if (request.getActive() != null) {
            shop.setActive(request.getActive());
        }

        WholesaleShop updated =
                wholesaleShopRepository.save(shop);

        log.info(
                "Wholesale shop updated successfully. shopId={}",
                id
        );

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        log.info(
                "Deleting wholesale shop. shopId={}",
                id
        );

        if (!wholesaleShopRepository.existsById(id)) {

            throw new RuntimeException(
                    "Wholesale shop not found"
            );
        }

        wholesaleShopRepository.deleteById(id);

        log.info(
                "Wholesale shop deleted successfully. shopId={}",
                id
        );
    }

    private WholesaleShopResponse mapToResponse(
            WholesaleShop shop) {

        return WholesaleShopResponse.builder()
                .id(shop.getId())
                .businessId(shop.getBusiness().getId())
                .shopName(shop.getShopName())
                .contactName(shop.getContactName())
                .phone(shop.getPhone())
                .email(shop.getEmail())
                .addressLine1(shop.getAddressLine1())
                .addressLine2(shop.getAddressLine2())
                .city(shop.getCity())
                .province(shop.getProvince())
                .postalCode(shop.getPostalCode())
                .active(shop.getActive())
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .build();
    }
}