package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.Loyalty.customer.CustomerLoyaltyResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyRedeemRuleResponse;
import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.enums.CustomerTier;
import com.buxbuddy.auth.enums.LoyaltyRuleType;
import com.buxbuddy.auth.enums.LoyaltyTransactionType;
import com.buxbuddy.auth.repository.*;
import com.buxbuddy.auth.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Transactional(readOnly = true)
    public CustomerLoyaltyResponse getCustomerByPhone(String phone) {

        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        // Total Transactions
        int totalTransactions = customer.getTransactions() != null
                ? customer.getTransactions().size()
                : 0;

        // Average Order Value
        double averageOrderValue = 0.0;
        if (customer.getVisitCount() != null
                && customer.getVisitCount() > 0
                && customer.getLifetimeSpend() != null) {

            averageOrderValue =
                    customer.getLifetimeSpend() / customer.getVisitCount();
        }

        // Customer Type
        String customerType;

        if (customer.getTier() != null
                && customer.getTier() != CustomerTier.NORMAL) {

            customerType = "VIP";

        } else if (customer.getVisitCount() != null
                && customer.getVisitCount() >= 20) {

            customerType = "Frequent";

        } else if (customer.getVisitCount() != null
                && customer.getVisitCount() <= 2) {

            customerType = "New";

        } else if (customer.getLastVisit() != null) {

            long daysSinceLastVisit =
                    java.time.temporal.ChronoUnit.DAYS.between(
                            customer.getLastVisit().toLocalDate(),
                            java.time.LocalDate.now());

            if (daysSinceLastVisit >= 90) {
                customerType = "Inactive";
            } else if (daysSinceLastVisit >= 30) {
                customerType = "At Risk";
            } else {
                customerType = "Regular";
            }

        } else {
            customerType = "New";
        }

        return CustomerLoyaltyResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .phone(customer.getPhone())

                // Loyalty
                .loyaltyPoints(customer.getLoyaltyPoints())
                .redeemableAmount(customer.getRedeemableAmount())
                .tier(customer.getTier())

                // Spending
                .monthlySpend(customer.getMonthlySpend())
                .lifetimeSpend(customer.getLifetimeSpend())

                // Visits
                .visitCount(customer.getVisitCount())
                .firstVisit(customer.getFirstVisit())
                .lastVisit(customer.getLastVisit())

                // Classification
                .customerType(customerType)

                // Address
                .city(customer.getCity())
                .province(customer.getProvince())
                .postalCode(customer.getPostalCode())
                .country(customer.getCountry())

                // Birthday
                .birthdayMonth(customer.getBirthdayMonth())

                // Statistics
                .totalTransactions(totalTransactions)
                .averageOrderValue(averageOrderValue)

                // Business
                .businessId(
                        customer.getBusiness() != null
                                ? customer.getBusiness().getId()
                                : null
                )

                // Audit
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())

                .build();
    }



    @Override
    @Transactional
    public LoyaltyEarnResponse earnPoints(
            LoyaltyEarnRequest request) {

        // 1. Find customer
        Customer customer =
                customerRepository.findByPhone(request.getPhone())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                ));


        // Validate purchase amount
        if (request.getPurchaseAmount() == null ||
                request.getPurchaseAmount()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Purchase amount must be greater than zero"
            );
        }


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

        BigDecimal earnedCashbackAmount =
                BigDecimal.ZERO;


        // 3. Calculate cashback and points

        BigDecimal minimumAmount =
                earnRule.getMinimumPurchaseAmount() == null
                        ? BigDecimal.ZERO
                        : earnRule.getMinimumPurchaseAmount();


        if (request.getPurchaseAmount()
                .compareTo(minimumAmount) >= 0) {


            if (earnRule.getCashbackPercentage() == null) {

                throw new RuntimeException(
                        "Cashback percentage is not configured"
                );
            }


        /*
            Example:

            Purchase = $350
            Cashback = 2%

            350 * 2 / 100 = $7

            $7 * 1000 = 7000 points
        */


            earnedCashbackAmount =
                    request.getPurchaseAmount()
                            .multiply(
                                    earnRule.getCashbackPercentage()
                            )
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP
                            );


            earnedPoints =
                    earnedCashbackAmount
                            .multiply(
                                    BigDecimal.valueOf(1000)
                            )
                            .intValue();



            // Maximum point limit
            if (earnRule.getMaxPoints() != null &&
                    earnedPoints > earnRule.getMaxPoints()) {

                earnedPoints =
                        earnRule.getMaxPoints();


                // recalculate cashback after max points
                earnedCashbackAmount =
                        BigDecimal.valueOf(earnedPoints)
                                .divide(
                                        BigDecimal.valueOf(1000),
                                        2,
                                        RoundingMode.HALF_UP
                                );
            }
        }



        // 4. Update customer points

        Integer currentPoints =
                customer.getLoyaltyPoints() == null
                        ? 0
                        : customer.getLoyaltyPoints();


        Integer updatedPoints =
                currentPoints + earnedPoints;


        customer.setLoyaltyPoints(
                updatedPoints
        );


        // Update redeemable dollar balance

        BigDecimal currentRedeemable =
                customer.getRedeemableAmount() == null
                        ? BigDecimal.ZERO
                        : customer.getRedeemableAmount();


        customer.setRedeemableAmount(
                currentRedeemable.add(
                        earnedCashbackAmount
                )
        );



        customer.setVisitCount(
                customer.getVisitCount() == null
                        ? 1
                        : customer.getVisitCount() + 1
        );


        customer.setLastVisit(
                LocalDateTime.now()
        );



        customer.setLifetimeSpend(
                (customer.getLifetimeSpend() == null
                        ? 0.0
                        : customer.getLifetimeSpend())
                        +
                        request.getPurchaseAmount()
                                .doubleValue()
        );


        customerRepository.save(customer);



        // 5. Save loyalty transaction

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
                        .earnedCashbackAmount(
                                earnedCashbackAmount
                        )
                        .balanceAfterTransaction(
                                updatedPoints
                        )
                        .purchaseAmount(
                                request.getPurchaseAmount()
                        )
                        .description(
                                "Purchase cashback"
                        )
                        .transactionDate(
                                LocalDateTime.now()
                        )
                        .createdDate(
                                LocalDateTime.now()
                        )
                        .build();


        loyaltyTransactionRepository.save(transaction);



        // 6. Response

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
                .redeemableAmount(
                        customer.getRedeemableAmount()
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
