package com.buxbuddy.service;

import com.buxbuddy.dto.ProductDto;
import com.buxbuddy.entity.Product;
import com.buxbuddy.entity.Vendor;
import com.buxbuddy.enums.ProductStatus;
import com.buxbuddy.repository.ProductRepository;
import com.buxbuddy.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public ProductDto addProduct(ProductDto dto) {

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        Product product = new Product();

        product.setProductName(dto.getProductName());
        product.setBarcode(dto.getBarcode());
        product.setCategory(dto.getCategory());
        product.setWeight(dto.getWeight());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setStock(dto.getStock());
        product.setReorderLevel(dto.getReorderLevel());
        product.setExpiryDate(dto.getExpiryDate());
        product.setStatus(ProductStatus.NORMAL);
        product.setCreatedAt(LocalDateTime.now());
        product.setVendor(vendor);
        Product saved = productRepository.save(product);
        dto.setId(saved.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setStatus(saved.getStatus());
        return dto;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, ProductDto dto) {

        Product product = getProductById(id);

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        product.setProductName(dto.getProductName());
        product.setBarcode(dto.getBarcode());
        product.setCategory(dto.getCategory());
        product.setWeight(dto.getWeight());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setStock(dto.getStock());
        product.setReorderLevel(dto.getReorderLevel());
        product.setExpiryDate(dto.getExpiryDate());
        product.setVendor(vendor);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}