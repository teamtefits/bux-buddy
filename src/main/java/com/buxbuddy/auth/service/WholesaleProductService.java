package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductResponse;

import java.util.List;

public interface WholesaleProductService {

    WholesaleProductResponse create(
            WholesaleProductRequest request
    );

    WholesaleProductResponse getById(
            Long id
    );

    List<WholesaleProductResponse> getByBusiness(
            Long businessId
    );

    WholesaleProductResponse update(
            Long id,
            WholesaleProductRequest request
    );

    void delete(Long id);
}
