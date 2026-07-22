package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.product.ProductRequest;
import com.buxbuddy.auth.dto.product.ProductResponse;
import com.buxbuddy.auth.dto.product.TaxCalculationResponse;
import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.enums.PackageType;
import com.buxbuddy.auth.enums.StockAction;
import com.buxbuddy.auth.enums.StockStatus;
import com.buxbuddy.auth.repository.*;
import com.buxbuddy.auth.service.ProductService;
import com.buxbuddy.auth.service.TaxCalculationService;
import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ProductCategoryRepository productCategoryRepository;
    private final TaxCalculationService taxCalculationService;


    @Override
    @Transactional
    public ProductResponse save(ProductRequest dto) {

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        ProductCategory category = productCategoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Product category not found"));

        int stock = dto.getCurrentStock() == null
                ? 0
                : dto.getCurrentStock();

        Product product = Product.builder()
                .productName(dto.getProductName())
                .productCode(dto.getItemCode())
                .brand(dto.getBrand())
                .category(category)
                .batchNumber(dto.getBatchNumber())
                .weight(dto.getWeight())
                .cost(dto.getWholesalePrice())
                .retailPrice(dto.getRetailPrice())
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
                .wholesalePrice(savedProduct.getCost())
                .retailPrice(savedProduct.getRetailPrice())
                .createdAt(LocalDateTime.now())
                .build();

        stockHistoryRepository.save(history);

        return mapToDto(savedProduct);
    }
    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int previousStock = product.getCurrentStock();

        int newStock = dto.getCurrentStock() == null
                ? 0
                : dto.getCurrentStock();

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        ProductCategory category = productCategoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Product category not found"));

        product.setProductName(dto.getProductName());
        product.setProductCode(dto.getItemCode());
        product.setBrand(dto.getBrand());
        product.setCategory(category);
        product.setBatchNumber(dto.getBatchNumber());
        product.setWeight(dto.getWeight());
        product.setCost(dto.getWholesalePrice());
        product.setRetailPrice(dto.getRetailPrice());
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

        if (previousStock != newStock) {

            StockHistory history = StockHistory.builder()
                    .product(updatedProduct)
                    .business(updatedProduct.getBusiness())
                    .previousStock(previousStock)
                    .newStock(newStock)
                    .quantityChanged(newStock - previousStock)
                    .action(StockAction.ADJUSTMENT)
                    .remarks("Product Updated")
                    .wholesalePrice(updatedProduct.getCost())
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
        TaxCalculationResponse tax =
                taxCalculationService.calculate(product);
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .itemCode(product.getProductCode())
                .brand(product.getBrand())
                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )
                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory().getCategoryName()
                                : null
                )
                .batchNumber(product.getBatchNumber())
                .weight(product.getWeight())
                .wholesalePrice(product.getCost())
                .retailPrice(product.getRetailPrice())
                .margin(product.getMargin())
                .marginPercentage(product.getMarginPercentage())
                // TAX
                .gstAmount(tax.getGstAmount())
                .provincialTaxAmount(
                        tax.getProvincialTaxAmount()
                )
                .carbonTaxAmount(
                        tax.getCarbonTaxAmount()
                )
                .totalTax(
                        tax.getTotalTax()
                )
                // DEPOSIT
                .depositApplicable(
                        tax.getDepositApplicable()
                )
                .depositAmount(
                        tax.getDepositAmount()
                )
                // FINAL PRICE
                .finalPrice(
                        tax.getFinalPrice()
                )
                .currentStock(product.getCurrentStock())
                .stockStatus(
                        product.getStockStatus().name()
                )
                .expiryDate(product.getExpiryDate())
                .deliveryDate(product.getDeliveryDate())
                .vendorId(
                        product.getVendor().getId()
                )
                .vendorName(
                        product.getVendor().getVendorName()
                )
                .businessId(
                        product.getBusiness().getId()
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

    @Override
    @Transactional
    public void uploadProducts(MultipartFile file) {

        List<Product> products = new ArrayList<>();

        try (
                CSVReader reader = new CSVReader(
                        new InputStreamReader(file.getInputStream())
                )
        ) {

            String[] row;
            boolean isHeader = true;

            while ((row = reader.readNext()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                final String[] currentRow = row;

                Business business = businessRepository
                        .findByBusinessName(currentRow[0])
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Business not found: " + currentRow[0]
                                )
                        );

                ProductCategory category = productCategoryRepository
                        .findByCategoryName(currentRow[4])
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found: " + currentRow[4]
                                )
                        );

                Vendor vendor = vendorRepository
                        .findByVendorNameAndBusinessId(
                                currentRow[16],
                                business.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vendor not found: " + currentRow[16]
                                )
                        );


                Product product = Product.builder()
                        .productName(currentRow[1])
                        .productCode(currentRow[2])
                        .brand(currentRow[3])
                        .category(category)
                        .batchNumber(currentRow[5])
                        .business(business)
                        .vendor(vendor)
                        .build();


                // Weight
                if (!currentRow[6].isBlank()) {
                    product.setWeight(
                            Double.parseDouble(currentRow[6])
                    );
                }


                // Volume
                if (!currentRow[7].isBlank()) {
                    product.setVolume(
                            Double.parseDouble(currentRow[7])
                    );
                }


                // Package Type
                if (!currentRow[8].isBlank()) {
                    product.setPackageType(
                            PackageType.valueOf(
                                    currentRow[8]
                                            .trim()
                                            .toUpperCase()
                            )
                    );
                }


                // Cost
                if (!currentRow[9].isBlank()) {
                    product.setCost(
                            new BigDecimal(currentRow[9])
                    );
                }


                // Retail Price
                if (!currentRow[10].isBlank()) {
                    product.setRetailPrice(
                            new BigDecimal(currentRow[10])
                    );
                }


                // Tax Applicable
                if (!currentRow[11].isBlank()) {
                    product.setTaxApplicable(
                            Boolean.parseBoolean(currentRow[11])
                    );
                }


                // Current Stock
                Integer stock = 0;

                if (!currentRow[12].isBlank()) {
                    stock = Integer.parseInt(currentRow[12]);
                }

                product.setCurrentStock(stock);


                // Stock Status
                if (!currentRow[13].isBlank()) {

                    product.setStockStatus(
                            StockStatus.valueOf(
                                    currentRow[13]
                                            .trim()
                                            .toUpperCase()
                            )
                    );

                } else {

                    product.setStockStatus(
                            stock > 0
                                    ? StockStatus.IN_STOCK
                                    : StockStatus.OUT_OF_STOCK
                    );
                }


                // Expiry Date
                if (!currentRow[14].isBlank()) {

                    product.setExpiryDate(
                            LocalDate.parse(currentRow[14])
                    );
                }


                // Delivery Date
                if (!currentRow[15].isBlank()) {

                    product.setDeliveryDate(
                            LocalDate.parse(currentRow[15])
                    );
                }


                products.add(product);
            }


            productRepository.saveAll(products);


        } catch (Exception e) {

            throw new RuntimeException(
                    "CSV upload failed: " + e.getMessage(),
                    e
            );
        }
    }
}