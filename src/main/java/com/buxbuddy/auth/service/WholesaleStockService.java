package com.buxbuddy.auth.service;



import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockResponse;

import java.math.BigDecimal;
import java.util.List;

public interface WholesaleStockService {

    WholesaleStockResponse create(
            Long businessId,
            WholesaleStockRequest request
    );

    WholesaleStockResponse getById(
            Long businessId,
            Long stockId
    );

    List<WholesaleStockResponse> getAll(
            Long businessId
    );

    WholesaleStockResponse update(
            Long businessId,
            Long stockId,
            WholesaleStockRequest request
    );

    void delete(
            Long businessId,
            Long stockId
    );

    WholesaleStockResponse addStock(
            Long businessId,
            Long productId,
            BigDecimal quantity
    );

    WholesaleStockResponse reduceStock(
            Long businessId,
            Long productId,
            BigDecimal quantity
    );
}