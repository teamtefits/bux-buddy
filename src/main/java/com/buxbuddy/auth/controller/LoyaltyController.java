package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.Loyalty.customer.CustomerLoyaltyResponse;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnRequest;
import com.buxbuddy.auth.dto.Loyalty.earn.LoyaltyEarnResponse;
import com.buxbuddy.auth.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {


    private final LoyaltyService loyaltyService;
    @GetMapping("/customer/{phone}")
    public ResponseEntity<CustomerLoyaltyResponse> getCustomerByPhone(
            @PathVariable String phone) {
        return ResponseEntity.ok(
                loyaltyService.getCustomerByPhone(phone)
        );
    }

    @PostMapping("/earn")
    public ResponseEntity<LoyaltyEarnResponse> earnPoints(
            @RequestBody LoyaltyEarnRequest request) {
        return ResponseEntity.ok(
                loyaltyService.earnPoints(request)
        );
    }

}
