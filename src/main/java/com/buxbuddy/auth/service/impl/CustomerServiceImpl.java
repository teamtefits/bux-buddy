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
                // Address Information
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                // Personal Information
                .birthdayMonth(request.getBirthdayMonth())
                // Business Connection
                .business(business)
                // User Connection
                .user(savedUser)
                // Default values
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
                .phone(customer.getPhone())

                // Email from User table
                .email(
                        customer.getUser() != null
                                ? customer.getUser().getEmail()
                                : null
                )

                // Address Information
                .addressLine1(customer.getAddressLine1())
                .addressLine2(customer.getAddressLine2())
                .city(customer.getCity())
                .province(customer.getProvince())
                .postalCode(customer.getPostalCode())
                .country(customer.getCountry())

                // Personal Information
                .birthdayMonth(customer.getBirthdayMonth())
                // Business Information
                .businessId(
                        customer.getBusiness() != null
                                ? customer.getBusiness().getId()
                                : null
                )
                .businessName(
                        customer.getBusiness() != null
                                ? customer.getBusiness().getBusinessName()
                                : null
                )
                // User connection
                .userId(
                        customer.getUser() != null
                                ? customer.getUser().getId()
                                : null
                )
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
    @Override
    @Transactional
    public CustomerResponse updateCustomer(
            Long customerId,
            CustomerRequest request
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
        // Update Customer Profile
        customer.setCustomerName(
                request.getCustomerName()
        );
        customer.setPhone(
                request.getPhone()
        );
        // Address
        customer.setAddressLine1(
                request.getAddressLine1()
        );
        customer.setAddressLine2(
                request.getAddressLine2()
        );
        customer.setCity(
                request.getCity()
        );
        customer.setProvince(
                request.getProvince()
        );
        customer.setPostalCode(
                request.getPostalCode()
        );
        customer.setCountry(
                request.getCountry()
        );
        // Personalization
        customer.setBirthdayMonth(
                request.getBirthdayMonth()
        );
        // Update login email in User table
        if (customer.getUser() != null
                && request.getEmail() != null) {
            customer.getUser()
                    .setEmail(request.getEmail());
            userRepository.save(customer.getUser());
        }
        customer.setUpdatedAt(
                LocalDateTime.now()
        );
        Customer updatedCustomer =
                customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }
    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
        // Remove user connection
        if (customer.getUser() != null) {
            User user = customer.getUser();
            customer.setUser(null);
            userRepository.delete(user);
        }
        customerRepository.delete(customer);
    }
}