package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopResponse;
import com.buxbuddy.auth.service.WholesaleShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wholesale-shops")
@RequiredArgsConstructor
public class WholesaleShopController {

    private final WholesaleShopService wholesaleShopService;

    @PostMapping
    public ResponseEntity<WholesaleShopResponse> create(
            @RequestBody WholesaleShopRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(wholesaleShopService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleShopResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                wholesaleShopService.getById(id)
        );
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<WholesaleShopResponse>> getByBusiness(
            @PathVariable Long businessId) {
        return ResponseEntity.ok(
                wholesaleShopService.getByBusiness(businessId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WholesaleShopResponse> update(
            @PathVariable Long id,
            @RequestBody WholesaleShopRequest request) {
        return ResponseEntity.ok(
                wholesaleShopService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        wholesaleShopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
