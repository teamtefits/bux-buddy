package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceResponse;

import java.util.List;

public interface WholesaleShopProductPriceService {

    WholesaleShopProductPriceResponse create(
            WholesaleShopProductPriceRequest request
    );

    WholesaleShopProductPriceResponse getById(
            Long id
    );

    List<WholesaleShopProductPriceResponse> getByShop(
            Long shopId
    );

    List<WholesaleShopProductPriceResponse> getByWholesaleProduct(
            Long wholesaleProductId
    );

    WholesaleShopProductPriceResponse update(
            Long id,
            WholesaleShopProductPriceRequest request
    );

    void delete(Long id);
}