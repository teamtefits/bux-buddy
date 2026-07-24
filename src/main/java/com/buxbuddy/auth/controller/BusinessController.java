package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.api.ApiResponseBuilder;
import com.buxbuddy.auth.dto.api.ApiSuccessResponse;
import com.buxbuddy.auth.dto.business.BusinessRequest;
import com.buxbuddy.auth.dto.business.BusinessResponse;
import com.buxbuddy.auth.service.BusinessService;
import com.buxbuddy.util.ApiResponseUtil;
import com.buxbuddy.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final MessageUtil messageUtil;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BusinessResponse>> create(
            @RequestBody BusinessRequest request,
            HttpServletRequest httpRequest) {
        return ApiResponseUtil.success(
                HttpStatus.CREATED,
                messageUtil.getMessage("business.created"),
                businessService.createBusiness(request),
                httpRequest
        );
    }
    @GetMapping("/{businessId}")
    public ResponseEntity<ApiSuccessResponse<BusinessResponse>> getById(
            @PathVariable Long businessId,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("business.retrieved"),
                        businessService.getBusinessById(businessId),
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<BusinessResponse>>> getAll(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("business.list.retrieved"),
                        businessService.getAllBusinesses(),
                        request
                )
        );
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<ApiSuccessResponse<BusinessResponse>> update(
            @PathVariable Long businessId,
            @Valid @RequestBody BusinessRequest requestBody,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("business.updated"),
                        businessService.updateBusiness(
                                businessId,
                                requestBody
                        ),
                        request
                )
        );
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(
            @PathVariable Long businessId,
            HttpServletRequest request) {

        businessService.deleteBusiness(businessId);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("business.deleted"),
                        null,
                        request
                )
        );
    }
}