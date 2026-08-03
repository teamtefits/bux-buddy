package com.buxbuddy.auth.service;


import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockHistoryRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.stock.WholesaleStockHistoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface WholesaleStockHistoryService {

    WholesaleStockHistoryResponse create(
            Long businessId,
            WholesaleStockHistoryRequest request
    );

    WholesaleStockHistoryResponse getById(
            Long businessId,
            Long historyId
    );

    List<WholesaleStockHistoryResponse> getAll(
            Long businessId
    );

    List<WholesaleStockHistoryResponse> getByProduct(
            Long businessId,
            Long wholesaleProductId
    );

    WholesaleStockHistoryResponse getByProductAndDate(
            Long businessId,
            Long wholesaleProductId,
            LocalDate stockDate
    );

    WholesaleStockHistoryResponse update(
            Long businessId,
            Long historyId,
            WholesaleStockHistoryRequest request
    );

    void delete(
            Long businessId,
            Long historyId
    );
}