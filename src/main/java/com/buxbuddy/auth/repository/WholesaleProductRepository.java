package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.WholesaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleProductRepository
        extends JpaRepository<WholesaleProduct, Long> {

    List<WholesaleProduct> findByBusinessIdAndActiveTrue(
            Long businessId
    );

    Optional<WholesaleProduct> findByBusinessIdAndProductName(
            Long businessId,
            String productName
    );

    boolean existsByBusinessIdAndProductName(
            Long businessId,
            String productName
    );
}