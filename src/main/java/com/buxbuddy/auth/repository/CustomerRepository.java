package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
        boolean existsByPhone(String customerPhone);
        Optional<Customer> findByPhone(String phone);
        List<Customer> findByBusinessId(Long businessId);
}