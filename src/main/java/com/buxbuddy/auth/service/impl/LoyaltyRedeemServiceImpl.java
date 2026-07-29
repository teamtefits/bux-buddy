package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.Loyalty.redeem.LoyaltyRedeemRequest;
import com.buxbuddy.auth.dto.Loyalty.redeem.LoyaltyRedeemResponse;
import com.buxbuddy.auth.entity.Customer;
import com.buxbuddy.auth.entity.LoyaltyTransaction;
import com.buxbuddy.auth.enums.LoyaltyTransactionType;
import com.buxbuddy.auth.repository.CustomerRepository;
import com.buxbuddy.auth.repository.LoyaltyTransactionRepository;
import com.buxbuddy.auth.service.LoyaltyRedeemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyRedeemServiceImpl implements LoyaltyRedeemService {


    private final CustomerRepository customerRepository;

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;


    @Override
    @Transactional
    public LoyaltyRedeemResponse redeemPoints(
            LoyaltyRedeemRequest request) {


        Customer customer = customerRepository
                .findByPhone(request.getPhone())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));


        BigDecimal redeemAmount = request.getRedeemAmount();


        if (redeemAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Invalid redeem amount");
        }


        if (customer.getRedeemableAmount()
                .compareTo(redeemAmount) < 0) {

            throw new RuntimeException(
                    "Insufficient redeemable balance");
        }


        // 1000 points = $1
        int pointsToDeduct =
                redeemAmount
                        .multiply(BigDecimal.valueOf(1000))
                        .intValue();


        if (customer.getLoyaltyPoints() < pointsToDeduct) {
            throw new RuntimeException(
                    "Insufficient loyalty points");
        }


        // Update customer points
        customer.setLoyaltyPoints(
                customer.getLoyaltyPoints()
                        - pointsToDeduct
        );


        // Update redeemable dollar amount
        customer.setRedeemableAmount(
                customer.getRedeemableAmount()
                        .subtract(redeemAmount)
        );


        customerRepository.save(customer);



        LoyaltyTransaction transaction =
                LoyaltyTransaction.builder()
                        .customer(customer)
                        .business(customer.getBusiness())
                        .transactionType(
                                LoyaltyTransactionType.REDEEM)
                        .points(-pointsToDeduct)
                        .balanceAfterTransaction(
                                customer.getLoyaltyPoints())
                        .redeemValue(redeemAmount)
                        .description(
                                "Loyalty points redeemed")
                        .transactionDate(
                                LocalDateTime.now())
                        .createdDate(
                                LocalDateTime.now())
                        .build();


        loyaltyTransactionRepository.save(transaction);



        return LoyaltyRedeemResponse.builder()
                .customerName(
                        customer.getCustomerName())
                .redeemedPoints(
                        pointsToDeduct)
                .redeemedAmount(
                        redeemAmount)
                .remainingPoints(
                        customer.getLoyaltyPoints())
                .remainingRedeemableAmount(
                        customer.getRedeemableAmount())
                .message(
                        "Points redeemed successfully")
                .build();
    }
}