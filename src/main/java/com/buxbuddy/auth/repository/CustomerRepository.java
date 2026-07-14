package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
        boolean existsByPhone(String customerPhone);
}