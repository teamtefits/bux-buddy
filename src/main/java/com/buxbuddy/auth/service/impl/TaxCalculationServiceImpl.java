package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.product.TaxCalculationResponse;
import com.buxbuddy.auth.entity.DepositRule;
import com.buxbuddy.auth.entity.Product;
import com.buxbuddy.auth.entity.Province;
import com.buxbuddy.auth.entity.TaxRule;
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
public class TaxCalculationServiceImpl
        implements TaxCalculationService {

    private final TaxRuleRepository taxRuleRepository;
    private final DepositRuleRepository depositRuleRepository;


    /**
     * Existing method.
     *
     * Used when calculating tax for a single product.
     */
    @Override
    public TaxCalculationResponse calculate(Product product) {

        Province province =
                product.getBusiness().getProvince();

        List<TaxRule> taxRules =
                taxRuleRepository.findActiveRulesByProvinceId(
                        province.getId()
                );

        List<DepositRule> depositRules =
                depositRuleRepository.findActiveRulesByProvinceId(
                        province.getId()
                );

        return calculate(
                product,
                taxRules,
                depositRules
        );
    }


    /**
     * Optimized method.
     *
     * Tax rules and deposit rules are already loaded.
     * NO DATABASE QUERY happens here.
     */
    @Override
    public TaxCalculationResponse calculate(
            Product product,
            List<TaxRule> taxRules,
            List<DepositRule> depositRules
    ) {

        BigDecimal price =
                product.getRetailPrice() != null
                        ? product.getRetailPrice()
                        : BigDecimal.ZERO;


        BigDecimal gstAmount =
                BigDecimal.ZERO;

        BigDecimal provincialTaxAmount =
                BigDecimal.ZERO;

        BigDecimal carbonTaxAmount =
                BigDecimal.ZERO;


        // ============================================================
        // TAX CALCULATION
        // ============================================================

        for (TaxRule rule : taxRules) {

            BigDecimal taxAmount;

            if ("PERCENTAGE".equals(
                    rule.getCalculationType().name())) {

                taxAmount =
                        price
                                .multiply(rule.getRate())
                                .divide(
                                        BigDecimal.valueOf(100),
                                        4,
                                        RoundingMode.HALF_UP
                                );

            } else {

                taxAmount =
                        rule.getRate() != null
                                ? rule.getRate()
                                : BigDecimal.ZERO;
            }


            String taxType =
                    rule.getTaxType().getName();


            switch (taxType) {

                case "GST":

                    gstAmount =
                            gstAmount.add(taxAmount);

                    break;


                case "PST":
                case "HST":
                case "SALES_TAX":

                    provincialTaxAmount =
                            provincialTaxAmount
                                    .add(taxAmount);

                    break;


                case "CARBON_TAX":

                    carbonTaxAmount =
                            carbonTaxAmount
                                    .add(taxAmount);

                    break;


                default:
                    break;
            }
        }


        // ============================================================
        // DEPOSIT CALCULATION
        // ============================================================

        BigDecimal depositAmount =
                BigDecimal.ZERO;

        boolean depositApplicable =
                false;


        Double volume =
                product.getWeight() != null
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
                    && Boolean.TRUE.equals(
                    rule.getDepositApplicable())) {

                depositApplicable = true;

                depositAmount =
                        rule.getAmount() != null
                                ? rule.getAmount()
                                : BigDecimal.ZERO;

                break;
            }
        }


        // ============================================================
        // TOTAL
        // ============================================================

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

                .provincialTaxAmount(
                        provincialTaxAmount
                )

                .carbonTaxAmount(
                        carbonTaxAmount
                )

                .totalTax(totalTax)

                .depositApplicable(
                        depositApplicable
                )

                .depositAmount(
                        depositAmount
                )

                .finalPrice(
                        finalPrice
                )

                .build();
    }
}