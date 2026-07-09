package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.business.BusinessRequest;
import com.buxbuddy.auth.dto.business.BusinessResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.enums.RoleType;
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

    @Override
    @Transactional
    public BusinessResponse createBusiness(BusinessRequest request) {
        Business business = Business.builder()
                .name(request.getName())
                .businessType(request.getBusinessType())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status("ACTIVE")
                .build();
        Business savedBusiness = businessRepository.save(business);
        // 2. Find BUSINESS_OWNER role
        Role ownerRole = roleRepository
                .findByName(RoleType.BUSINESS_OWNER)
                .orElseThrow(
                        () -> new RuntimeException("Business owner role not found")
                );
        User owner = User.builder()
                .firstName(request.getName())
                .lastName(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()
                ))

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
                .name(business.getName())
                .businessType(business.getBusinessType())
                .email(business.getEmail())
                .phone(business.getPhone())
                .address(business.getAddress())
                .status(business.getStatus())
                .createdAt(business.getCreatedAt())
                .build();
    }
}