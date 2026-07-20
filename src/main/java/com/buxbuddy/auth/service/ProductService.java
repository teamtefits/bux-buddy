package com.buxbuddy.auth.service;


import com.buxbuddy.auth.dto.product.ProductRequest;
import com.buxbuddy.auth.dto.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponse save(ProductRequest dto);

    ProductResponse update(Long id, ProductRequest dto);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll(Long businessId);

    void delete(Long id);

    void uploadProducts(MultipartFile file);
}