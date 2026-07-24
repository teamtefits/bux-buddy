package com.buxbuddy.auth.service.impl;


import com.buxbuddy.auth.dto.vendor.VendorRequest;
import com.buxbuddy.auth.dto.vendor.VendorResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.Vendor;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.VendorRepository;
import com.buxbuddy.auth.service.VendorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public VendorResponse saveVendor(VendorRequest request) {

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() ->
                        new RuntimeException("Business not found")
                );
        Vendor vendor = Vendor.builder()
                .vendorName(request.getName())
                .vendorEmail(request.getEmail())
                .vendorPhone(request.getPhone())
                .vendorAddress(request.getAddress())
                .status("ACTIVE")
                .business(business)
                .build();
        Vendor savedVendor = vendorRepository.save(vendor);
        return VendorResponse.builder()
                .vendorId(savedVendor.getId())
                .name(savedVendor.getVendorName())
                .email(savedVendor.getVendorEmail())
                .phone(savedVendor.getVendorPhone())
                .address(savedVendor.getVendorAddress())
                .businessId(savedVendor.getBusiness().getId())
                .build();
    }

    @Override
    public List<VendorResponse> getVendorsByBusiness(Long businessId) {

        List<Vendor> vendors = vendorRepository.findByBusinessId(businessId);

        return vendors.stream()
                .map(this::mapToVendorResponse)
                .toList();
    }

    private VendorResponse mapToVendorResponse(Vendor vendor) {
        VendorResponse response = new VendorResponse();
        response.setVendorId(vendor.getId());
        response.setName(vendor.getVendorName());
        response.setEmail(vendor.getVendorEmail());
        response.setPhone(vendor.getVendorPhone());
        response.setBusinessId(vendor.getBusiness().getId());
        return response;
    }
    @Override
    @Transactional
    public VendorResponse updateVendor(
            Long vendorId,
            VendorRequest request
    ) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));
        vendor.setVendorName(request.getName());
        vendor.setVendorEmail(request.getEmail());
        vendor.setVendorPhone(request.getPhone());
        vendor.setVendorAddress(request.getAddress());
        vendor.setUpdatedAt(LocalDateTime.now());
        Vendor updatedVendor =
                vendorRepository.save(vendor);


        return mapToResponse(updatedVendor);
    }
    @Override
    @Transactional
    public void deleteVendor(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));


        vendorRepository.delete(vendor);
    }
    private VendorResponse mapToResponse(Vendor vendor) {

        return VendorResponse.builder()
                .vendorId(vendor.getId())
                .name(vendor.getVendorName())
                .email(vendor.getVendorEmail())
                .phone(vendor.getVendorPhone())
                .address(vendor.getVendorAddress())

                .businessId(
                        vendor.getBusiness() != null
                                ? vendor.getBusiness().getId()
                                : null
                )
                .businessName(
                        vendor.getBusiness() != null
                                ? vendor.getBusiness().getBusinessName()
                                : null
                )
                .createdAt(vendor.getCreatedAt())
                .updatedAt(vendor.getUpdatedAt())
                .build();
    }
}

