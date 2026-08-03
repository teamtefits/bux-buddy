package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopResponse;

import java.util.List;

public interface WholesaleShopService {

    WholesaleShopResponse create(
            WholesaleShopRequest request
    );

    WholesaleShopResponse getById(
            Long id
    );

    List<WholesaleShopResponse> getByBusiness(
            Long businessId
    );

    WholesaleShopResponse update(
            Long id,
            WholesaleShopRequest request
    );

    void delete(
            Long id
    );
}