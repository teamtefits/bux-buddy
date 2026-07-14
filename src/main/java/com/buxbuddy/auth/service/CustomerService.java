package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.customer.CustomerRequest;
import com.buxbuddy.auth.dto.customer.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse saveCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse recordVisit(Long customerId);
    List<CustomerResponse> getAllCustomers();
}