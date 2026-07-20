package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.product.ProductRequest;
import com.buxbuddy.auth.dto.product.ProductResponse;
import com.buxbuddy.auth.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> save(
            @RequestBody ProductRequest dto) {

        return ResponseEntity.ok(productService.save(dto));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> update(
            @RequestParam(name = "id") Long id,
            @RequestBody ProductRequest dto) {

        return ResponseEntity.ok(productService.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<ProductResponse> get(
            @RequestParam(name = "id") Long id) {

        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/business")
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(name = "businessId") Long businessId) {

        return ResponseEntity.ok(productService.getAll(businessId));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam(name = "id") Long id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch-upload")
    public ResponseEntity<String> uploadProducts(
            @RequestParam("file") MultipartFile file) {
        productService.uploadProducts(file);
        return ResponseEntity.ok(
                "Products uploaded successfully"
        );
    }
}
