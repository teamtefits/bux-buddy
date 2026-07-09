package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.product.ProductRequest;
import com.buxbuddy.auth.dto.product.ProductResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.Product;
import com.buxbuddy.auth.entity.StockHistory;
import com.buxbuddy.auth.entity.Vendor;
import com.buxbuddy.auth.enums.StockAction;
import com.buxbuddy.auth.enums.StockStatus;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.ProductRepository;
import com.buxbuddy.auth.repository.StockHistoryRepository;
import com.buxbuddy.auth.repository.VendorRepository;
import com.buxbuddy.auth.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final BusinessRepository businessRepository;
    private final StockHistoryRepository stockHistoryRepository;

    @Override
    public ProductResponse save(ProductRequest dto) {
        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
        int stock = dto.getCurrentStock() == null
                ? 0
                : dto.getCurrentStock();
        Product product = Product.builder()
                .productName(dto.getProductName())
                .itemCode(dto.getItemCode())
                .brand(dto.getBrand())
                .department(dto.getDepartment())
                .batchNumber(dto.getBatchNumber())
                .weight(dto.getWeight())
                .wholesalePrice(dto.getWholesalePrice())
                .retailPrice(dto.getRetailPrice())
                .gstApplicable(dto.getGstApplicable())
                .currentStock(stock)
                .expiryDate(dto.getExpiryDate())
                .deliveryDate(dto.getDeliveryDate())
                .vendor(vendor)
                .business(business)
                .stockStatus(
                        stock > 0
                                ? StockStatus.IN_STOCK
                                : StockStatus.OUT_OF_STOCK
                )
                .build();
        Product savedProduct = productRepository.save(product);
        StockHistory history = StockHistory.builder()
                .product(savedProduct)
                .business(business)
                .previousStock(0)
                .newStock(stock)
                .quantityChanged(stock)
                .action(StockAction.PURCHASE)
                .remarks("Initial Stock")
                .wholesalePrice(savedProduct.getWholesalePrice())
                .retailPrice(savedProduct.getRetailPrice())
                .createdAt(LocalDateTime.now())
                .build();
        stockHistoryRepository.save(history);
        return mapToDto(savedProduct);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int previousStock = product.getCurrentStock();
        int newStock = dto.getCurrentStock() == null
                ? 0
                : dto.getCurrentStock();
        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        product.setProductName(dto.getProductName());
        product.setItemCode(dto.getItemCode());
        product.setBrand(dto.getBrand());
        product.setDepartment(dto.getDepartment());
        product.setBatchNumber(dto.getBatchNumber());
        product.setWeight(dto.getWeight());
        product.setWholesalePrice(dto.getWholesalePrice());
        product.setRetailPrice(dto.getRetailPrice());
        product.setGstApplicable(dto.getGstApplicable());
        product.setCurrentStock(newStock);
        product.setExpiryDate(dto.getExpiryDate());
        product.setDeliveryDate(dto.getDeliveryDate());
        product.setVendor(vendor);
        product.setStockStatus(
                newStock > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK
        );
        Product updatedProduct = productRepository.save(product);
        if(previousStock != newStock) {
            StockHistory history = StockHistory.builder()
                    .product(updatedProduct)
                    .business(updatedProduct.getBusiness())
                    .previousStock(previousStock)
                    .newStock(newStock)
                    .quantityChanged(newStock - previousStock)
                    .action(StockAction.ADJUSTMENT)
                    .remarks("Product Updated")
                    .wholesalePrice(updatedProduct.getWholesalePrice())
                    .retailPrice(updatedProduct.getRetailPrice())
                    .createdAt(LocalDateTime.now())
                    .build();
            stockHistoryRepository.save(history);
        }
        return mapToDto(updatedProduct);
    }
    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDto(product);
    }

    @Override
    public List<ProductResponse> getAll(Long businessId) {
        return productRepository.findByBusinessId(businessId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }





    private ProductResponse mapToDto(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .itemCode(product.getItemCode())
                .brand(product.getBrand())
                .department(product.getDepartment())
                .batchNumber(product.getBatchNumber())
                .weight(product.getWeight())
                .wholesalePrice(product.getWholesalePrice())
                .retailPrice(product.getRetailPrice())
                .margin(product.getMargin())
                .marginPercentage(product.getMarginPercentage())
                .gstApplicable(product.getGstApplicable())
                .currentStock(product.getCurrentStock())
                .stockStatus(
                        product.getStockStatus() != null
                                ? product.getStockStatus().name()
                                : null
                )
                .expiryDate(product.getExpiryDate())
                .deliveryDate(product.getDeliveryDate())
                .vendorName(
                        product.getVendor() != null
                                ? product.getVendor().getVendorName()
                                : null
                )

                .vendorId(
                        product.getVendor() != null
                                ? product.getVendor().getId()
                                : null
                )
                .businessId(
                        product.getBusiness() != null
                                ? product.getBusiness().getId()
                                : null
                )
                .build();
    }
    public void biWeeklyStockUpdate(Long productId, int newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int oldStock = product.getCurrentStock();
        product.setCurrentStock(newStock);
        product.setStockStatus(
                newStock > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK
        );
        productRepository.save(product);
        StockHistory history = StockHistory.builder()
                .product(product)
                .business(product.getBusiness())
                .previousStock(oldStock)
                .newStock(newStock)
                .quantityChanged(newStock - oldStock)
                .action(StockAction.COUNT)
                .remarks("Bi Weekly Stock Count")
                .createdAt(LocalDateTime.now())
                .build();
        stockHistoryRepository.save(history);
    }
}