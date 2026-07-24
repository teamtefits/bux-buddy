package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.vendor.VendorRequest;
import com.buxbuddy.auth.dto.vendor.VendorResponse;
import com.buxbuddy.auth.entity.Vendor;

import java.util.List;

public interface VendorService {

    VendorResponse saveVendor(VendorRequest request);
    List<VendorResponse> getVendorsByBusiness(Long businessId);
    VendorResponse updateVendor(Long vendorId, VendorRequest request);
    void deleteVendor(Long vendorId);

}