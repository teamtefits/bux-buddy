package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.customer.CustomerRequest;
import com.buxbuddy.auth.dto.customer.CustomerResponse;
import com.buxbuddy.auth.entity.Business;
import com.buxbuddy.auth.entity.Customer;
import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.enums.CustomerTier;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.BusinessRepository;
import com.buxbuddy.auth.repository.CustomerRepository;
import com.buxbuddy.auth.repository.RoleRepository;
import com.buxbuddy.auth.repository.UserRepository;
import com.buxbuddy.auth.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponse saveCustomer(CustomerRequest request) {

        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Customer phone already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));

        Role customerRole = roleRepository.findByName(RoleType.CUSTOMER)
                .orElseThrow(() ->
                        new RuntimeException("CUSTOMER role not found"));

        User user = User.builder()
                .firstName(request.getCustomerName())
                .lastName("")
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(customerRole))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        User savedUser = userRepository.save(user);

        Customer customer = Customer.builder()
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .birthdayMonth(request.getBirthdayMonth())
                .business(business)
                .user(savedUser)
                .loyaltyPoints(0)
                .visitCount(0)
                .monthlySpend(0.0)
                .lifetimeSpend(0.0)
                .tier(CustomerTier.NORMAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse recordVisit(Long customerId) {
        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );
        // Increment visit count
        customer.setVisitCount(
                customer.getVisitCount() + 1
        );
        // Update last visit date/time
        customer.setLastVisit(
                LocalDateTime.now()
        );
        // Add loyalty points
        customer.setLoyaltyPoints(
                customer.getLoyaltyPoints() + 10
        );
        Customer updatedCustomer =
                customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );
        return mapToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CustomerResponse mapToResponse(Customer customer) {

        return CustomerResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .email(customer.getUser() != null
                        ? customer.getUser().getEmail()
                        : null)
                .phone(customer.getPhone())
                .birthdayMonth(customer.getBirthdayMonth())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .visitCount(customer.getVisitCount())
                .lastVisit(customer.getLastVisit())
                .tier(customer.getTier())
                .monthlySpend(customer.getMonthlySpend())
                .lifetimeSpend(customer.getLifetimeSpend())
                .businessId(customer.getBusiness() != null
                        ? customer.getBusiness().getId()
                        : null)
                .businessName(customer.getBusiness() != null
                        ? customer.getUser().getFirstName()
                        : null)
                .userId(customer.getUser() != null
                        ? customer.getUser().getId()
                        : null)
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByBusiness(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new RuntimeException("Business not found")
                );

        List<Customer> customers =
                customerRepository.findByBusinessId(businessId);

        return customers.stream()
                .map(this::mapToResponse)
                .toList();
    }
}