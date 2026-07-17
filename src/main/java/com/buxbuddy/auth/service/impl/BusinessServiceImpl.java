package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.business.BusinessRequest;
import com.buxbuddy.auth.dto.business.BusinessResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.BusinessCategory;
import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.BusinessCategoryRepository;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.RoleRepository;
import com.buxbuddy.auth.repository.UserRepository;
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

    @Override
    @Transactional
    public BusinessResponse createBusiness(BusinessRequest request) {
        // 1. Find business category
        BusinessCategoryType type = BusinessCategoryType.valueOf(request.getBusinessCategory());

        BusinessCategory category = businessCategoryRepository
                .findByName(type)
                .orElseThrow(() ->
                        new RuntimeException("Business category not found"));
        // 2. Create business
        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .businessCategory(category)
                .businessEmail(request.getBusinessEmail())
                .businessPhone(request.getBusinessPhone())
                .businessAddress(request.getBusinessAddress())
                .status("ACTIVE")
                .build();


        Business savedBusiness = businessRepository.save(business);
        // 3. Find BUSINESS_OWNER role
        Role ownerRole = roleRepository
                .findByName(RoleType.BUSINESS_OWNER)
                .orElseThrow(() ->
                        new RuntimeException("Business owner role not found")
                );
        // 4. Create owner user
        User owner = User.builder()
                .firstName(request.getBusinessName())
                .lastName(request.getBusinessName())
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
        return BusinessResponse.builder()
                .id(business.getId())
                .name(business.getBusinessName())
                .businessType(
                        business.getBusinessCategory() != null
                                ? business.getBusinessCategory().getName().name()
                                : null
                )
                .email(business.getBusinessEmail())
                .phone(business.getBusinessPhone())
                .address(business.getBusinessAddress())
                .status(business.getStatus())
                .createdAt(business.getCreatedAt())
                .build();
    }
}