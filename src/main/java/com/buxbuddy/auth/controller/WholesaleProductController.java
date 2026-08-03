package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.WholesaleProductResponse;
import com.buxbuddy.auth.service.WholesaleProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wholesale-products")
@RequiredArgsConstructor
@Slf4j
public class WholesaleProductController {

    private final WholesaleProductService wholesaleProductService;

    @PostMapping
    public ResponseEntity<WholesaleProductResponse> create(
            @RequestBody WholesaleProductRequest request) {

        log.info(
                "POST /api/wholesale-products"
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        wholesaleProductService.create(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleProductResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                wholesaleProductService.getById(id)
        );
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<WholesaleProductResponse>> getByBusiness(
            @PathVariable Long businessId) {

        return ResponseEntity.ok(
                wholesaleProductService
                        .getByBusiness(businessId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WholesaleProductResponse> update(
            @PathVariable Long id,
            @RequestBody WholesaleProductRequest request) {

        return ResponseEntity.ok(
                wholesaleProductService
                        .update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        wholesaleProductService.delete(id);

        return ResponseEntity.noContent().build();
    }
}