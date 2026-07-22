package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.product.TaxCalculationResponse;
import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.repository.DepositRuleRepository;
import com.buxbuddy.auth.repository.TaxRuleRepository;
import com.buxbuddy.auth.service.TaxCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaxCalculationServiceImpl implements TaxCalculationService {
    private final TaxRuleRepository taxRuleRepository;
    private final DepositRuleRepository depositRuleRepository;
    @Override
    public TaxCalculationResponse calculate(Product product) {
        BigDecimal price = product.getRetailPrice() != null
                ? product.getRetailPrice()
                : BigDecimal.ZERO;
        BigDecimal gstAmount = BigDecimal.ZERO;
        BigDecimal provincialTaxAmount = BigDecimal.ZERO;
        BigDecimal carbonTaxAmount = BigDecimal.ZERO;
        Province province =
                product.getBusiness()
                        .getProvince();
        // Tax Calculation
        List<TaxRule> taxRules =
                taxRuleRepository
                        .findByProvinceAndActiveTrue(province);
        for (TaxRule rule : taxRules) {
            BigDecimal taxAmount;
            if (rule.getCalculationType().name()
                    .equals("PERCENTAGE")) {
                taxAmount =
                        price
                                .multiply(rule.getRate())
                                .divide(
                                        BigDecimal.valueOf(100),
                                        4,
                                        RoundingMode.HALF_UP
                                );
            } else {
                taxAmount = rule.getRate();
            }
            switch (rule.getTaxType().getName()) {
                case "GST":
                    gstAmount = gstAmount.add(taxAmount);
                    break;
                case "PST":
                case "HST":
                case "SALES_TAX":
                    provincialTaxAmount =
                            provincialTaxAmount.add(taxAmount);
                    break;
                case "CARBON_TAX":
                    carbonTaxAmount =
                            carbonTaxAmount.add(taxAmount);
                    break;
            }
        }
        // Deposit Calculation
        BigDecimal depositAmount = BigDecimal.ZERO;
        boolean depositApplicable = false;
        List<DepositRule> depositRules =
                depositRuleRepository
                        .findByProvinceAndActiveTrue(province);
        // NULL weight will become 0.0
        Double volume = product.getWeight() != null
                ? product.getWeight()
                : 0.0;
        for (DepositRule rule : depositRules) {
            boolean minimumMatch =
                    rule.getMinimumVolume() == null
                            ||
                            volume >= rule.getMinimumVolume();
            boolean maximumMatch =
                    rule.getMaximumVolume() == null
                            ||
                            volume <= rule.getMaximumVolume();
            if (minimumMatch
                    && maximumMatch
                    && Boolean.TRUE.equals(rule.getDepositApplicable())) {
                depositApplicable = true;
                depositAmount =
                        rule.getAmount() != null
                                ? rule.getAmount()
                                : BigDecimal.ZERO;
                break;
            }
        }
        BigDecimal totalTax =
                gstAmount
                        .add(provincialTaxAmount)
                        .add(carbonTaxAmount);
        BigDecimal finalPrice =
                price
                        .add(totalTax)
                        .add(depositAmount);
        return TaxCalculationResponse.builder()
                .gstAmount(gstAmount)
                .provincialTaxAmount(provincialTaxAmount)
                .carbonTaxAmount(carbonTaxAmount)
                .totalTax(totalTax)
                .depositApplicable(depositApplicable)
                .depositAmount(depositAmount)
                .finalPrice(finalPrice)
                .build();
    }
}