package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository
        extends JpaRepository<Province, Long> {
    Optional<Province> findByProvinceCode(String provinceCode);
}