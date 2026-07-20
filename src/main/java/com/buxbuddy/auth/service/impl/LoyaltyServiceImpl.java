package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.Loyalty.customer.CustomerLoyaltyResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleResponse;
import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.enums.LoyaltyRuleType;
import com.buxbuddy.auth.enums.LoyaltyTransactionType;
import com.buxbuddy.auth.repository.*;
import com.buxbuddy.auth.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {


    private final CustomerRepository customerRepository;

    private final LoyaltyEarnRuleRepository loyaltyEarnRuleRepository;

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    private final LoyaltyRedeemRuleRepository loyaltyRedeemRuleRepository;

    private final BusinessRepository businessRepository;

    @Override
    public CustomerLoyaltyResponse getCustomerByPhone(String phone) {

        Customer customer =
                customerRepository.findByPhone(phone)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                ));


        return CustomerLoyaltyResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .phone(customer.getPhone())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .tier(customer.getTier())
                .businessId(
                        customer.getBusiness().getId()
                )
                .build();
    }



    @Override
    public LoyaltyEarnResponse earnPoints(
            LoyaltyEarnRequest request) {
        // 1. Find customer by phone
        Customer customer =
                customerRepository.findByPhone(request.getPhone())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                ));
        // 2. Get DEFAULT earning rule
        LoyaltyEarnRule earnRule =
                loyaltyEarnRuleRepository
                        .findByBusiness_IdAndRuleTypeAndActiveTrue(
                                customer.getBusiness().getId(),
                                LoyaltyRuleType.DEFAULT
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Default loyalty rule not found"
                                ));
        Integer earnedPoints = 0;
        // 3. Calculate points
        if (request.getPurchaseAmount()
                .compareTo(
                        earnRule.getMinimumPurchaseAmount()
                ) >= 0) {
            earnedPoints =
                    request.getPurchaseAmount()
                            .multiply(
                                    earnRule.getMultiplier()
                            )
                            .intValue();
            // Maximum point limit
            if (earnRule.getMaxPoints() != null &&
                    earnedPoints > earnRule.getMaxPoints()) {

                earnedPoints =
                        earnRule.getMaxPoints();
            }
        }
        // 4. Update customer points
        Integer currentPoints =
                customer.getLoyaltyPoints() == null
                        ? 0
                        : customer.getLoyaltyPoints();
        Integer updatedPoints =
                currentPoints + earnedPoints;
        customer.setLoyaltyPoints(updatedPoints);
        customer.setVisitCount(
                customer.getVisitCount() == null
                        ? 1
                        : customer.getVisitCount() + 1
        );
        customer.setLastVisit(
                LocalDateTime.now()
        );
        customerRepository.save(customer);
        // 5. Get redeem rule

        LoyaltyRedeemRule redeemRule =
                loyaltyRedeemRuleRepository
                        .findByBusiness_IdAndActiveTrue(
                                customer.getBusiness().getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Redeem rule not found"
                                ));
        // 6. Calculate reward value

        BigDecimal rewardValue =
                BigDecimal.ZERO;
        if (earnedPoints > 0) {
            rewardValue =
                    BigDecimal.valueOf(earnedPoints)
                            .divide(
                                    BigDecimal.valueOf(
                                            redeemRule.getPointsRequired()
                                    )
                            )
                            .multiply(
                                    redeemRule.getDiscountValue()
                            );
        }
        // 7. Save loyalty transaction

        LoyaltyTransaction transaction =
                LoyaltyTransaction.builder()
                        .customer(customer)
                        .business(customer.getBusiness())
                        .transactionType(
                                LoyaltyTransactionType.EARN
                        )
                        .points(
                                earnedPoints
                        )
                        .purchaseAmount(
                                request.getPurchaseAmount()
                        )
                        .balanceAfterTransaction(
                                updatedPoints
                        )
                        .description(
                                "Purchase reward"
                        )
                        .transactionDate(
                                LocalDateTime.now()
                        )
                        .build();
        loyaltyTransactionRepository.save(transaction);
        // 8. Response
        return LoyaltyEarnResponse.builder()
                .customerName(
                        customer.getCustomerName()
                )
                .purchaseAmount(
                        request.getPurchaseAmount()
                )
                .earnedPoints(
                        earnedPoints
                )
                .rewardValue(
                        rewardValue
                )
                .totalPoints(
                        updatedPoints
                )
                .message(
                        "Points earned successfully"
                )
                .build();
    }

    @Override
    public LoyaltyRedeemRuleResponse createRule(
            LoyaltyRedeemRuleRequest request) {
        Business business =
                businessRepository.findById(
                                request.getBusinessId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Business not found"
                                ));
        LoyaltyRedeemRule rule =
                LoyaltyRedeemRule.builder()
                        .pointsRequired(
                                request.getPointsRequired()
                        )
                        .discountValue(
                                request.getDiscountValue()
                        )
                        .type(
                                request.getType()
                        )
                        .active(
                                request.getActive() == null
                                        ? true
                                        : request.getActive()
                        )
                        .business(business)
                        .build();

        LoyaltyRedeemRule saved =
                loyaltyRedeemRuleRepository.save(rule);

        return LoyaltyRedeemRuleResponse.builder()
                .id(saved.getId())
                .pointsRequired(
                        saved.getPointsRequired()
                )
                .discountValue(
                        saved.getDiscountValue()
                )
                .type(
                        saved.getType()
                )
                .active(
                        saved.getActive()
                )
                .businessId(
                        saved.getBusiness().getId()
                )
                .build();
    }
}
