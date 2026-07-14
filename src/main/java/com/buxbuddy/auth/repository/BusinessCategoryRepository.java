package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.BusinessCategory;
import com.buxbuddy.auth.enums.BusinessCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {
    Optional<BusinessCategory> findByName(BusinessCategoryType businessCategoryType);
}