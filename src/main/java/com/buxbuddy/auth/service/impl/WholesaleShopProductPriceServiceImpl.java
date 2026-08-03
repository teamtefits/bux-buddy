package com.buxbuddy.auth.service.impl;


import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceResponse;
import com.buxbuddy.auth.entity.WholesaleProduct;
import com.buxbuddy.auth.entity.WholesaleShop;
import com.buxbuddy.auth.entity.WholesaleShopProductPrice;
import com.buxbuddy.auth.repository.WholesaleProductRepository;
import com.buxbuddy.auth.repository.WholesaleShopProductPriceRepository;
import com.buxbuddy.auth.repository.WholesaleShopRepository;
import com.buxbuddy.auth.service.WholesaleShopProductPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WholesaleShopProductPriceServiceImpl
        implements WholesaleShopProductPriceService {

    private final WholesaleShopProductPriceRepository priceRepository;
    private final WholesaleShopRepository shopRepository;
    private final WholesaleProductRepository wholesaleProductRepository;

    @Override
    public com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceResponse create(
            com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceRequest request) {

        log.info(
                "Creating shop product price. shopId={}, wholesaleProductId={}",
                request.getShopId(),
                request.getWholesaleProductId()
        );

        WholesaleShop shop =
                shopRepository.findById(request.getShopId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale shop not found"
                                ));

        WholesaleProduct product =
                wholesaleProductRepository.findById(
                                request.getWholesaleProductId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wholesale product not found"
                                ));

        if (priceRepository
                .existsByShopIdAndWholesaleProductId(
                        request.getShopId(),
                        request.getWholesaleProductId())) {

            throw new RuntimeException(
                    "Price already exists for this shop and wholesale product"
            );
        }

        WholesaleShopProductPrice price =
                WholesaleShopProductPrice.builder()
                        .shop(shop)
                        .wholesaleProduct(product)
                        .sellingPrice(request.getSellingPrice())
                        .active(
                                request.getActive() == null
                                        ? true
                                        : request.getActive()
                        )
                        .build();

        WholesaleShopProductPrice saved =
                priceRepository.save(price);

        log.info(
                "Shop product price created. id={}",
                saved.getId()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleShopProductPriceResponse getById(Long id) {

        log.info(
                "Fetching shop product price. id={}",
                id
        );

        WholesaleShopProductPrice price =
                priceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Shop product price not found"
                                ));

        return mapToResponse(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleShopProductPriceResponse> getByShop(
            Long shopId) {

        log.info(
                "Fetching product prices for shop. shopId={}",
                shopId
        );

        return priceRepository
                .findByShopIdOrderByIdDesc(shopId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleShopProductPriceResponse>
    getByWholesaleProduct(Long wholesaleProductId) {

        log.info(
                "Fetching shop prices for wholesale product. wholesaleProductId={}",
                wholesaleProductId
        );

        return priceRepository
                .findByWholesaleProductIdOrderByIdDesc(
                        wholesaleProductId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public WholesaleShopProductPriceResponse update(
            Long id,
            WholesaleShopProductPriceRequest request) {

        log.info(
                "Updating shop product price. id={}",
                id
        );

        WholesaleShopProductPrice price =
                priceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Shop product price not found"
                                ));

        /*
         * Normally we don't change shop/product during update.
         * We only update the price and active status.
         */

        price.setSellingPrice(
                request.getSellingPrice()
        );

        if (request.getActive() != null) {
            price.setActive(request.getActive());
        }

        WholesaleShopProductPrice updated =
                priceRepository.save(price);

        log.info(
                "Shop product price updated successfully. id={}",
                id
        );

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        log.info(
                "Deleting shop product price. id={}",
                id
        );

        if (!priceRepository.existsById(id)) {

            throw new RuntimeException(
                    "Shop product price not found"
            );
        }

        priceRepository.deleteById(id);

        log.info(
                "Shop product price deleted successfully. id={}",
                id
        );
    }

    private WholesaleShopProductPriceResponse mapToResponse(
            WholesaleShopProductPrice entity) {

        return WholesaleShopProductPriceResponse.builder()
                .id(entity.getId())

                .shopId(
                        entity.getShop().getId()
                )
                .shopName(
                        entity.getShop().getShopName()
                )

                .wholesaleProductId(
                        entity.getWholesaleProduct().getId()
                )
                .productName(
                        entity.getWholesaleProduct().getProductName()
                )

                .sellingPrice(
                        entity.getSellingPrice()
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