package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.api.ApiSuccessResponse;
import com.buxbuddy.auth.dto.customer.CustomerRequest;
import com.buxbuddy.auth.dto.customer.CustomerResponse;
import com.buxbuddy.auth.service.CustomerService;
import com.buxbuddy.util.ApiResponseUtil;
import com.buxbuddy.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final MessageUtil messageUtil;
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<CustomerResponse>> create(
            @RequestBody CustomerRequest request,
            HttpServletRequest httpRequest) {
        return ApiResponseUtil.success(
                HttpStatus.CREATED,
                messageUtil.getMessage("customer.created"),
                customerService.saveCustomer(request),
                httpRequest
        );
    }
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<CustomerResponse>>> getAllCustomers(
            HttpServletRequest httpRequest) {
        return ApiResponseUtil.success(
                HttpStatus.OK,
                messageUtil.getMessage("customer.found"),
                customerService.getAllCustomers(),
                httpRequest
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<CustomerResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        return ApiResponseUtil.success(
                HttpStatus.OK,
                messageUtil.getMessage("customer.found"),
                customerService.getCustomerById(id),
                httpRequest
        );
    }

    @PostMapping("/{id}/visit")
    public ResponseEntity<CustomerResponse> recordVisit(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                customerService.recordVisit(id)
        );
    }

}