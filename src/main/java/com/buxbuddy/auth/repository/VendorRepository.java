package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByBusinessId(Long businessId);
}