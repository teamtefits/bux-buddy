package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.customer.CustomerRequest;
import com.buxbuddy.auth.dto.customer.CustomerResponse;
import com.buxbuddy.auth.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponse saveCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse recordVisit(Long customerId);
    List<CustomerResponse> getAllCustomers();
    List<CustomerResponse> getCustomersByBusiness(Long businessId);
    CustomerResponse updateCustomer(Long customerId, CustomerRequest request);
    void deleteCustomer(Long customerId);
}