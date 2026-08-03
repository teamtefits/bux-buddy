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
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final BusinessRepository businessRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final TaxCalculationService taxCalculationService;
    private final TaxRuleRepository taxRuleRepository;
    private final DepositRuleRepository depositRuleRepository;


    // ============================================================
    // SAVE
    // ============================================================

    @Override
    @Transactional
    public ProductResponse save(ProductRequest dto) {

        log.info("Creating product: {}", dto.getProductName());

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));

        ProductCategory category =
                productCategoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product category not found"
                                ));

        int stock = dto.getCurrentStock() == null
                ? 0
                : dto.getCurrentStock();

        Product product = Product.builder()
                .productName(dto.getProductName())
                .productCode(dto.getProductCode())
                .brand(dto.getBrand())
                .category(category)
                .batchNumber(dto.getBatchNumber())
                .weight(dto.getWeight())
                .cost(dto.getCost())
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

        Product savedProduct =
                productRepository.save(product);

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

        log.info(
                "Product created successfully. Product ID: {}",
                savedProduct.getId()
        );

        return mapToDto(savedProduct);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @Override
    @Transactional
    public ProductResponse update(
            Long id,
            ProductRequest dto
    ) {

        log.info("Updating product ID: {}", id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        int previousStock =
                product.getCurrentStock();

        int newStock =
                dto.getCurrentStock() == null
                        ? 0
                        : dto.getCurrentStock();

        Vendor vendor =
                vendorRepository.findById(dto.getVendorId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vendor not found"
                                ));

        ProductCategory category =
                productCategoryRepository
                        .findById(dto.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product category not found"
                                ));

        product.setProductName(
                dto.getProductName()
        );

        product.setProductCode(
                dto.getProductCode()
        );

        product.setBrand(
                dto.getBrand()
        );

        product.setCategory(
                category
        );

        product.setBatchNumber(
                dto.getBatchNumber()
        );

        product.setWeight(
                dto.getWeight()
        );

        product.setCost(
                dto.getCost()
        );

        product.setRetailPrice(
                dto.getRetailPrice()
        );

        product.setCurrentStock(
                newStock
        );

        product.setExpiryDate(
                dto.getExpiryDate()
        );

        product.setDeliveryDate(
                dto.getDeliveryDate()
        );

        product.setVendor(
                vendor
        );

        product.setStockStatus(
                newStock > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK
        );

        Product updatedProduct =
                productRepository.save(product);


        // ========================================================
        // STOCK HISTORY
        // ========================================================

        if (previousStock != newStock) {

            StockHistory history =
                    StockHistory.builder()
                            .product(updatedProduct)
                            .business(
                                    updatedProduct.getBusiness()
                            )
                            .previousStock(
                                    previousStock
                            )
                            .newStock(
                                    newStock
                            )
                            .quantityChanged(
                                    newStock - previousStock
                            )
                            .action(
                                    StockAction.ADJUSTMENT
                            )
                            .remarks(
                                    "Product Updated"
                            )
                            .wholesalePrice(
                                    updatedProduct.getCost()
                            )
                            .retailPrice(
                                    updatedProduct.getRetailPrice()
                            )
                            .createdAt(
                                    LocalDateTime.now()
                            )
                            .build();

            stockHistoryRepository.save(history);
        }

        log.info(
                "Product updated successfully. Product ID: {}",
                id
        );

        return mapToDto(updatedProduct);
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    public ProductResponse getById(Long id) {

        log.info("Getting product ID: {}", id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        return mapToDto(product);
    }


    // ============================================================
    // GET ALL PRODUCTS - OPTIMIZED
    // ============================================================

    @Override
    @Transactional
    public List<ProductResponse> getAll(Long businessId) {

        long start =
                System.currentTimeMillis();

        log.info("==============================================");
        log.info("GET ALL PRODUCTS START");
        log.info("Business ID: {}", businessId);


        // ========================================================
        // STEP 1: GET PRODUCTS
        // ========================================================

        long productStart =
                System.currentTimeMillis();

        List<Product> products =
                productRepository.findByBusinessId(
                        businessId
                );

        long productEnd =
                System.currentTimeMillis();

        log.info(
                "Products found: {}",
                products.size()
        );

        log.info(
                "Product DB query time: {} ms",
                productEnd - productStart
        );


        if (products.isEmpty()) {

            log.info("No products found.");

            return List.of();
        }


        // ========================================================
        // STEP 2: GET PROVINCE ONCE
        // ========================================================

        long rulesStart =
                System.currentTimeMillis();

        Province province =
                products.get(0)
                        .getBusiness()
                        .getProvince();

        Long provinceId =
                province.getId();

        log.info(
                "Province ID: {}",
                provinceId
        );


        // ========================================================
        // STEP 3: LOAD TAX RULES ONCE
        // ========================================================

        List<TaxRule> taxRules =
                taxRuleRepository
                        .findActiveRulesByProvinceId(
                                provinceId
                        );

        log.info(
                "Tax rules loaded: {}",
                taxRules.size()
        );


        // ========================================================
        // STEP 4: LOAD DEPOSIT RULES ONCE
        // ========================================================

        List<DepositRule> depositRules =
                depositRuleRepository
                        .findActiveRulesByProvinceId(
                                provinceId
                        );

        log.info(
                "Deposit rules loaded: {}",
                depositRules.size()
        );


        long rulesEnd =
                System.currentTimeMillis();

        log.info(
                "Tax + Deposit rules loading time: {} ms",
                rulesEnd - rulesStart
        );


        // ========================================================
        // STEP 5: MAP PRODUCTS
        // ========================================================

        long mappingStart =
                System.currentTimeMillis();

        List<ProductResponse> response =
                products.stream()
                        .map(product ->
                                mapToDto(
                                        product,
                                        taxRules,
                                        depositRules
                                )
                        )
                        .collect(Collectors.toList());


        long mappingEnd =
                System.currentTimeMillis();

        log.info(
                "DTO mapping time: {} ms",
                mappingEnd - mappingStart
        );


        long totalTime =
                System.currentTimeMillis() - start;

        log.info(
                "Total getAll time: {} ms",
                totalTime
        );

        log.info("GET ALL PRODUCTS END");
        log.info("==============================================");


        return response;
    }


    // ============================================================
    // DELETE
    // ============================================================

    @Override
    public void delete(Long id) {

        log.info("Deleting product ID: {}", id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        productRepository.delete(product);

        log.info(
                "Product deleted successfully. ID: {}",
                id
        );
    }


    // ============================================================
    // NORMAL DTO MAPPING
    // ============================================================

    private ProductResponse mapToDto(
            Product product
    ) {

        TaxCalculationResponse tax =
                taxCalculationService.calculate(
                        product
                );

        return buildProductResponse(
                product,
                tax
        );
    }


    // ============================================================
    // OPTIMIZED DTO MAPPING
    // IMPORTANT:
    // NO DATABASE QUERY FOR TAX/DEPOSIT HERE
    // ============================================================

    private ProductResponse mapToDto(
            Product product,
            List<TaxRule> taxRules,
            List<DepositRule> depositRules
    ) {

        TaxCalculationResponse tax =
                taxCalculationService.calculate(
                        product,
                        taxRules,
                        depositRules
                );

        return buildProductResponse(
                product,
                tax
        );
    }


    // ============================================================
    // BUILD RESPONSE
    // ============================================================

    private ProductResponse buildProductResponse(
            Product product,
            TaxCalculationResponse tax
    ) {

        return ProductResponse.builder()

                .id(product.getId())

                .productName(
                        product.getProductName()
                )

                .itemCode(
                        product.getProductCode()
                )

                .brand(
                        product.getBrand()
                )

                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )

                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory()
                                .getCategoryName()
                                : null
                )

                .batchNumber(
                        product.getBatchNumber()
                )

                .weight(
                        product.getWeight()
                )

                .wholesalePrice(
                        product.getCost()
                )

                .retailPrice(
                        product.getRetailPrice()
                )

                .margin(
                        product.getMargin()
                )

                .marginPercentage(
                        product.getMarginPercentage()
                )

                // TAX
                .gstAmount(
                        tax.getGstAmount()
                )

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

                .currentStock(
                        product.getCurrentStock()
                )

                .stockStatus(
                        product.getStockStatus() != null
                                ? product.getStockStatus().name()
                                : null
                )

                .expiryDate(
                        product.getExpiryDate()
                )

                .deliveryDate(
                        product.getDeliveryDate()
                )

                .vendorId(
                        product.getVendor() != null
                                ? product.getVendor().getId()
                                : null
                )

                .vendorName(
                        product.getVendor() != null
                                ? product.getVendor().getVendorName()
                                : null
                )

                .businessId(
                        product.getBusiness() != null
                                ? product.getBusiness().getId()
                                : null
                )

                .build();
    }


    // ============================================================
    // BI-WEEKLY STOCK UPDATE
    // ============================================================

    public void biWeeklyStockUpdate(
            Long productId,
            int newStock
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        int oldStock =
                product.getCurrentStock();

        product.setCurrentStock(
                newStock
        );

        product.setStockStatus(
                newStock > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK
        );

        productRepository.save(product);


        StockHistory history =
                StockHistory.builder()
                        .product(product)
                        .business(
                                product.getBusiness()
                        )
                        .previousStock(
                                oldStock
                        )
                        .newStock(
                                newStock
                        )
                        .quantityChanged(
                                newStock - oldStock
                        )
                        .action(
                                StockAction.COUNT
                        )
                        .remarks(
                                "Bi Weekly Stock Count"
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        stockHistoryRepository.save(history);
    }


    // ============================================================
    // CSV UPLOAD
    // ============================================================

    @Override
    @Transactional
    public void uploadProducts(
            MultipartFile file
    ) {

        List<Product> products =
                new ArrayList<>();

        try (
                CSVReader reader =
                        new CSVReaderBuilder(
                                new InputStreamReader(
                                        file.getInputStream()
                                )
                        )
                                .withCSVParser(
                                        new CSVParserBuilder()
                                                .withSeparator(',')
                                                .build()
                                )
                                .build()
        ) {

            String[] row;

            boolean isHeader = true;

            while (
                    (row = reader.readNext()) != null
            ) {

                // Skip header
                if (isHeader) {

                    isHeader = false;

                    continue;
                }


                // Skip empty rows
                if (row.length == 0) {
                    continue;
                }


                // Validate column count
                if (row.length < 17) {

                    throw new RuntimeException(
                            "Invalid CSV row. Expected 17 columns but found "
                                    + row.length
                    );
                }


                log.info(
                        "Columns: {}",
                        row.length
                );

                log.info(
                        "CSV Row: {}",
                        Arrays.toString(row)
                );


                // =================================================
                // BUSINESS
                // =================================================

                final String businessName =
                        row[0].trim();

                Optional<Business> businessOptional =
                        businessRepository
                                .findByBusinessNameIgnoreCase(
                                        businessName
                                );

                if (businessOptional.isEmpty()) {

                    throw new RuntimeException(
                            "Business not found: "
                                    + businessName
                    );
                }

                Business business =
                        businessOptional.get();


                // =================================================
                // CATEGORY
                // =================================================

                final String categoryName =
                        row[4].trim();

                Optional<ProductCategory>
                        categoryOptional =
                        productCategoryRepository
                                .findByCategoryName(
                                        categoryName
                                );

                if (categoryOptional.isEmpty()) {

                    throw new RuntimeException(
                            "Category not found: "
                                    + categoryName
                    );
                }

                ProductCategory category =
                        categoryOptional.get();


                // =================================================
                // VENDOR
                // =================================================

                final String vendorName =
                        row[16].trim();

                Optional<Vendor> vendorOptional =
                        vendorRepository
                                .findByVendorNameAndBusinessId(
                                        vendorName,
                                        business.getId()
                                );

                if (vendorOptional.isEmpty()) {

                    throw new RuntimeException(
                            "Vendor not found: "
                                    + vendorName
                    );
                }

                Vendor vendor =
                        vendorOptional.get();


                // =================================================
                // PRODUCT
                // =================================================

                Product product =
                        Product.builder()

                                .business(business)

                                .productName(
                                        row[1].trim()
                                )

                                .productCode(
                                        row[2].trim()
                                )

                                .brand(
                                        row[3].trim()
                                )

                                .category(
                                        category
                                )

                                .batchNumber(
                                        row[5].trim()
                                )

                                .vendor(
                                        vendor
                                )

                                .build();


                // Weight
                if (!row[6].isBlank()) {

                    product.setWeight(
                            Double.parseDouble(
                                    row[6].trim()
                            )
                    );
                }


                // Volume
                if (!row[7].isBlank()) {

                    product.setVolume(
                            Double.parseDouble(
                                    row[7].trim()
                            )
                    );
                }


                // Package Type
                if (!row[8].isBlank()) {

                    product.setPackageType(
                            PackageType.valueOf(
                                    row[8]
                                            .trim()
                                            .toUpperCase()
                            )
                    );
                }


                // Cost
                if (!row[9].isBlank()) {

                    product.setCost(
                            new BigDecimal(
                                    row[9].trim()
                            )
                    );
                }


                // Retail Price
                if (!row[10].isBlank()) {

                    product.setRetailPrice(
                            new BigDecimal(
                                    row[10].trim()
                            )
                    );
                }


                // Tax Applicable
                if (!row[11].isBlank()) {

                    product.setTaxApplicable(
                            Boolean.parseBoolean(
                                    row[11].trim()
                            )
                    );
                }


                // Stock
                Integer stock = 0;

                if (!row[12].isBlank()) {

                    stock =
                            Integer.parseInt(
                                    row[12].trim()
                            );
                }

                product.setCurrentStock(stock);


                // Stock Status
                if (!row[13].isBlank()) {

                    product.setStockStatus(
                            StockStatus.valueOf(
                                    row[13]
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
                if (!row[14].isBlank()) {

                    product.setExpiryDate(
                            LocalDate.parse(
                                    row[14].trim()
                            )
                    );
                }


                // Delivery Date
                if (!row[15].isBlank()) {

                    product.setDeliveryDate(
                            LocalDate.parse(
                                    row[15].trim()
                            )
                    );
                }


                products.add(product);
            }


            // =====================================================
            // SAVE ALL
            // =====================================================

            productRepository.saveAll(
                    products
            );

            log.info(
                    "Successfully uploaded {} products",
                    products.size()
            );

        } catch (Exception e) {

            log.error(
                    "CSV upload failed",
                    e
            );

            throw new RuntimeException(
                    "CSV upload failed: "
                            + e.getMessage(),
                    e
            );
        }
    }
}