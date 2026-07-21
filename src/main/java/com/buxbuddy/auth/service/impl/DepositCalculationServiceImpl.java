package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.entity.DepositRule;
import com.buxbuddy.auth.entity.Product;
import com.buxbuddy.auth.entity.Province;
import com.buxbuddy.auth.repository.DepositRuleRepository;
import com.buxbuddy.auth.service.DepositCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DepositCalculationServiceImpl
        implements DepositCalculationService {
    private final DepositRuleRepository depositRuleRepository;
    @Override
    public BigDecimal calculate(Product product) {
        Province province =
                product.getBusiness()
                        .getProvince();

        List<DepositRule> rules =
                depositRuleRepository
                        .findByProvinceAndActiveTrue(province);
        Double volume =
                product.getWeight();
        for(DepositRule rule : rules){
            if(!rule.getDepositApplicable()){
                continue;
            }
            if(volume >= rule.getMinimumVolume()
                    &&
                    volume <= rule.getMaximumVolume()){
                return rule.getAmount();
            }
        }
        return BigDecimal.ZERO;
    }
}