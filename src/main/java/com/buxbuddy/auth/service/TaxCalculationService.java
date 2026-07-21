package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.product.TaxCalculationResponse;
import com.buxbuddy.auth.entity.Product;

public interface TaxCalculationService {

    TaxCalculationResponse calculate(Product product);

}