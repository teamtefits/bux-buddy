package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.business.BusinessRequest;
import com.buxbuddy.auth.dto.business.BusinessResponse;

import java.util.List;

public interface BusinessService {
    BusinessResponse createBusiness(BusinessRequest request);
    List<BusinessResponse> getAllBusinesses();
    BusinessResponse getBusinessById(Long id);
}