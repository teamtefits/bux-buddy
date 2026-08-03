package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceRequest;
import com.buxbuddy.auth.dto.wholesaleProduct.shop.WholesaleShopProductPriceResponse;
import com.buxbuddy.auth.service.WholesaleShopProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wholesale-shop-product-prices")
@RequiredArgsConstructor
public class WholesaleShopProductPriceController {

    private final WholesaleShopProductPriceService priceService;

    @PostMapping
    public ResponseEntity<WholesaleShopProductPriceResponse> create(
            @RequestBody WholesaleShopProductPriceRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(priceService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleShopProductPriceResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                priceService.getById(id)
        );
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<WholesaleShopProductPriceResponse>>
    getByShop(
            @PathVariable Long shopId) {

        return ResponseEntity.ok(
                priceService.getByShop(shopId)
        );
    }

    @GetMapping("/product/{wholesaleProductId}")
    public ResponseEntity<List<WholesaleShopProductPriceResponse>>
    getByWholesaleProduct(
            @PathVariable Long wholesaleProductId) {

        return ResponseEntity.ok(
                priceService.getByWholesaleProduct(
                        wholesaleProductId
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WholesaleShopProductPriceResponse> update(
            @PathVariable Long id,
            @RequestBody WholesaleShopProductPriceRequest request) {
        return ResponseEntity.ok(
                priceService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}