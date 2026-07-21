package com.buxbuddy.auth.service;

import com.buxbuddy.auth.entity.Product;

import java.math.BigDecimal;

public interface DepositCalculationService {

    BigDecimal calculate(Product product);

}
