package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {

    Optional<TaxType> findByCode(String code);

}