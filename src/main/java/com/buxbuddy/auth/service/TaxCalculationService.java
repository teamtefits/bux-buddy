package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.product.TaxCalculationResponse;
import com.buxbuddy.auth.entity.DepositRule;
import com.buxbuddy.auth.entity.Product;
import com.buxbuddy.auth.entity.TaxRule;

import java.util.List;

public interface TaxCalculationService {

    TaxCalculationResponse calculate(Product product);
    TaxCalculationResponse calculate(Product product, List<TaxRule> taxRules, List<DepositRule> depositRules
    );
}