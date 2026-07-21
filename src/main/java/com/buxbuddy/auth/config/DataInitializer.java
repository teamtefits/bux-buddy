package com.buxbuddy.auth.config;

import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.enums.TaxCalculationType;
import com.buxbuddy.auth.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final BusinessCategoryRepository businessCategoryRepository;

    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;

    private final TaxTypeRepository taxTypeRepository;
    private final TaxRuleRepository taxRuleRepository;

    private final DepositRuleRepository depositRuleRepository;

    private final ProductCategoryRepository productCategoryRepository;


    @Override
    public void run(String... args) {


        createRoles();

        createAdminUser();

        createBusinessCategories();

        createCountriesAndProvinces();

        createTaxTypes();

        createProductCategories();

        createTaxRules();

        createDepositRules();


    }


    // ==========================
    // ROLES
    // ==========================

    private void createRoles() {


        if (roleRepository.count() == 0) {


            roleRepository.save(
                    Role.builder()
                            .name(RoleType.ADMIN)
                            .build()
            );


            roleRepository.save(
                    Role.builder()
                            .name(RoleType.BUSINESS_OWNER)
                            .build()
            );


            roleRepository.save(
                    Role.builder()
                            .name(RoleType.EMPLOYEE)
                            .build()
            );


            roleRepository.save(
                    Role.builder()
                            .name(RoleType.CUSTOMER)
                            .build()
            );


            System.out.println("✅ Roles created");

        }

    }


    // ==========================
    // ADMIN USER
    // ==========================


    private void createAdminUser() {


        if (!userRepository.existsByEmail("admin@buxbuddy.com")) {


            Role role =
                    roleRepository.findByName(RoleType.ADMIN)
                            .orElseThrow();


            User user =
                    User.builder()
                            .firstName("System")
                            .lastName("Admin")
                            .email("admin@buxbuddy.com")
                            .password(
                                    passwordEncoder.encode(
                                            "Admin@123"
                                    )
                            )
                            .enabled(true)
                            .roles(Set.of(role))
                            .build();


            userRepository.save(user);


            System.out.println("✅ Admin created");

        }

    }


    // ==========================
    // BUSINESS CATEGORY
    // ==========================

    private void createBusinessCategories() {
        if (businessCategoryRepository.count() == 0) {
            for (
                    BusinessCategoryType type :
                    BusinessCategoryType.values()
            ) {

                businessCategoryRepository.save(
                        BusinessCategory.builder()
                                .name(type)
                                .build()
                );
            }
            System.out.println(
                    "✅ Business categories created"
            );
        }
    }

    // ==========================
    // COUNTRY + PROVINCE
    // ==========================
    private void createCountriesAndProvinces() {
        if (countryRepository.count() == 0) {
            Country canada =
                    countryRepository.save(

                            Country.builder()
                                    .countryName("Canada")
                                    .countryCode("CA")
                                    .build()

                    );
            provinceRepository.save(
                    Province.builder()
                            .provinceName("Alberta")
                            .provinceCode("AB")
                            .country(canada)
                            .build()
            );
            provinceRepository.save(
                    Province.builder()
                            .provinceName("Ontario")
                            .provinceCode("ON")
                            .country(canada)
                            .build()
            );
            provinceRepository.save(
                    Province.builder()
                            .provinceName("British Columbia")
                            .provinceCode("BC")
                            .country(canada)
                            .build()
            );
            Country usa =
                    countryRepository.save(

                            Country.builder()
                                    .countryName("United States")
                                    .countryCode("US")
                                    .build()
                    );
            provinceRepository.save(
                    Province.builder()
                            .provinceName("California")
                            .provinceCode("CA")
                            .country(usa)
                            .build()
            );
            provinceRepository.save(
                    Province.builder()
                            .provinceName("Texas")
                            .provinceCode("TX")
                            .country(usa)
                            .build()
            );
        }
    }

    private void createTaxTypes() {
        if (taxTypeRepository.count() == 0) {
            saveTaxType(
                    "GST",
                    "Goods and Services Tax",
                    "Federal GST"
            );
            saveTaxType(
                    "HST",
                    "Harmonized Sales Tax",
                    "Federal + Provincial tax"
            );
            saveTaxType(
                    "PST",
                    "Provincial Sales Tax",
                    "Provincial tax"
            );
            saveTaxType(
                    "CARBON_TAX",
                    "Carbon Tax",
                    "Environmental charge"
            );
            saveTaxType(
                    "DEPOSIT",
                    "Container Deposit",
                    "Bottle deposit"
            );
        }
    }

    private void saveTaxType(
            String code,
            String name,
            String description
    ) {
        taxTypeRepository.save(

                TaxType.builder()
                        .code(code)
                        .name(name)
                        .description(description)
                        .active(true)
                        .build()
        );
    }

    // ==========================
    // PRODUCT CATEGORY
    // ==========================
    private void createProductCategories() {
        if (productCategoryRepository.count() == 0) {
            String[] categories = {
                    "BEVERAGES",
                    "DAIRY",
                    "FROZEN_FOOD",
                    "GROCERY",
                    "SNACKS",
                    "FRUITS",
                    "VEGETABLES",
                    "MEAT",
                    "SEAFOOD",
                    "BAKERY",
                    "HOUSEHOLD",
                    "PERSONAL_CARE",
                    "ALCOHOL",
                    "TOBACCO",
                    "OTHER"

            };


            for (String category : categories) {


                productCategoryRepository.save(

                        ProductCategory.builder()
                                .categoryName(category)
                                .description(category)
                                .active(true)
                                .build()

                );

            }


            System.out.println(
                    "✅ Product categories created"
            );

        }

    }


    // ==========================
    // TAX RULES
    // ==========================


    private void createTaxRules() {


        if (taxRuleRepository.count() == 0) {


            Province alberta =
                    provinceRepository
                            .findByProvinceCode("AB")
                            .orElseThrow();


            TaxType gst =
                    taxTypeRepository
                            .findByCode("GST")
                            .orElseThrow();


            TaxType carbon =
                    taxTypeRepository
                            .findByCode("CARBON_TAX")
                            .orElseThrow();


            taxRuleRepository.save(

                    TaxRule.builder()
                            .taxName("GST")
                            .taxType(gst)
                            .calculationType(
                                    TaxCalculationType.PERCENTAGE
                            )
                            .rate(
                                    BigDecimal.valueOf(5)
                            )
                            .province(alberta)
                            .active(true)
                            .build()

            );


            taxRuleRepository.save(

                    TaxRule.builder()
                            .taxName("Carbon Tax")
                            .taxType(carbon)
                            .calculationType(
                                    TaxCalculationType.PERCENTAGE
                            )
                            .rate(
                                    BigDecimal.valueOf(2)
                            )
                            .province(alberta)
                            .active(true)
                            .build()

            );


            System.out.println(
                    "✅ Tax rules created"
            );

        }


    }


    // ==========================
    // DEPOSIT RULES
    // ==========================


    private void createDepositRules() {


        if (depositRuleRepository.count() == 0) {
            Province alberta =
                    provinceRepository
                            .findByProvinceCode("AB")
                            .orElseThrow();
            depositRuleRepository.save(
                    DepositRule.builder()
                            .name("No Deposit")
                            .amount(BigDecimal.ZERO)
                            .minimumVolume(0.0)
                            .maximumVolume(0.0)
                            .depositApplicable(false)
                            .province(alberta)
                            .active(true)
                            .build()
            );
            depositRuleRepository.save(
                    DepositRule.builder()
                            .name("Beverage Under 2L")
                            .amount(
                                    BigDecimal.valueOf(0.10)
                            )
                            .minimumVolume(0.01)
                            .maximumVolume(2.0)
                            .depositApplicable(true)
                            .province(alberta)
                            .active(true)
                            .build()

            );
            depositRuleRepository.save(
                    DepositRule.builder()
                            .name("Beverage Over 2L")
                            .amount(
                                    BigDecimal.valueOf(0.25)
                            )
                            .minimumVolume(2.01)
                            .maximumVolume(10.0)
                            .depositApplicable(true)
                            .province(alberta)
                            .active(true)
                            .build()
            );
            System.out.println(
                    "✅ Deposit rules created"
            );
        }
    }
}