package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.business.BusinessRequest;
import com.buxbuddy.auth.dto.business.BusinessResponse;
import com.buxbuddy.auth.dto.business.CountryResponse;
import com.buxbuddy.auth.dto.business.ProvinceResponse;
import com.buxbuddy.auth.entity.*;
import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.*;
import com.buxbuddy.auth.service.BusinessService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final ProvinceRepository provinceRepository;
    @Override
    @Transactional
    public BusinessResponse createBusiness(BusinessRequest request) {
        // 1. Find business category
        BusinessCategory category = businessCategoryRepository
                .findByName(request.getBusinessCategory())
                .orElseThrow(() ->
                        new RuntimeException("Business category not found")
                );

        // 2. Find Province
        Province province = provinceRepository
                .findById(request.getProvinceId())
                .orElseThrow(() ->
                        new RuntimeException("Province not found")
                );
        // 3. Create Business
        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .businessCategory(category)
                // Contact Information
                .businessEmail(request.getBusinessEmail())
                .businessPhone(request.getBusinessPhone())
                // Address Information
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .province(province)
                .postalCode(request.getPostalCode())

                // Tax Information
                .taxNumber(request.getTaxNumber())
                .taxEnabled(
                        request.getTaxEnabled() != null
                                ? request.getTaxEnabled()
                                : true
                )
                .status("ACTIVE")
                .build();
        Business savedBusiness = businessRepository.save(business);
        // 4. Find BUSINESS_OWNER role
        Role ownerRole = roleRepository
                .findByName(RoleType.BUSINESS_OWNER)
                .orElseThrow(() ->
                        new RuntimeException("Business owner role not found")
                );
        // 5. Create Owner User
        User owner = User.builder()
                .firstName(request.getBusinessName())
                .lastName("OWNER")
                .email(request.getBusinessEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .business(savedBusiness)
                .enabled(true)
                .build();
        owner.getRoles().add(ownerRole);
        userRepository.save(owner);
        return mapToResponse(savedBusiness);
    }
    @Override
    public List<BusinessResponse> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BusinessResponse getBusinessById(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));
        return mapToResponse(business);
    }

    private BusinessResponse mapToResponse(Business business) {
        Province province = business.getProvince();
        return BusinessResponse.builder()
                .id(business.getId())
                .name(business.getBusinessName())
                .businessType(
                        business.getBusinessCategory() != null
                                ? business.getBusinessCategory().getName()
                                : null
                )
                // Contact
                .email(business.getBusinessEmail())
                .phone(business.getBusinessPhone())
                // Address
                .addressLine1(business.getAddressLine1())
                .addressLine2(business.getAddressLine2())
                .city(business.getCity())
                .postalCode(business.getPostalCode())
                .province(
                        province != null
                                ? ProvinceResponse.builder()
                                .id(province.getId())
                                .provinceName(province.getProvinceName())
                                .provinceCode(province.getProvinceCode())
                                .country(
                                        CountryResponse.builder()
                                                .id(province.getCountry().getId())
                                                .countryName(
                                                        province.getCountry().getCountryName()
                                                )
                                                .countryCode(
                                                        province.getCountry().getCountryCode()
                                                )
                                                .build()
                                )
                                .build()
                                : null
                )
                // Tax
                .taxNumber(business.getTaxNumber())
                .taxEnabled(business.getTaxEnabled())
                // Status
                .status(business.getStatus())
                .createdAt(business.getCreatedAt())
                .updatedAt(business.getUpdatedAt())
                .build();
    }
}