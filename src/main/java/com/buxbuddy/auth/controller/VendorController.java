package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.api.ApiSuccessResponse;
import com.buxbuddy.auth.dto.customer.CustomerResponse;
import com.buxbuddy.auth.dto.vendor.VendorRequest;
import com.buxbuddy.auth.dto.vendor.VendorResponse;
import com.buxbuddy.auth.service.CustomerService;
import com.buxbuddy.auth.service.VendorService;
import com.buxbuddy.util.ApiResponseUtil;
import com.buxbuddy.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final MessageUtil messageUtil;
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<VendorResponse>> create(
            @RequestBody VendorRequest request,
            HttpServletRequest httpRequest) {
        return ApiResponseUtil.success(
                HttpStatus.CREATED,
                messageUtil.getMessage("vendor.created"),
                vendorService.saveVendor(request),
                httpRequest
        );
    }

    @GetMapping("/business")
    public ResponseEntity<ApiSuccessResponse<List<VendorResponse>>> getVendorsByBusiness(
            @RequestParam(name = "businessId") Long businessId,
            HttpServletRequest httpRequest) {

        return ApiResponseUtil.success(
                HttpStatus.OK,
                messageUtil.getMessage("vendor.list"),
                vendorService.getVendorsByBusiness(businessId),
                httpRequest
        );
    }
}