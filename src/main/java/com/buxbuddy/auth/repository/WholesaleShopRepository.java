package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.WholesaleShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface WholesaleShopRepository
        extends JpaRepository<WholesaleShop, Long> {

    boolean existsByBusinessIdAndShopName(
            Long businessId,
            String shopName
    );
    Optional<WholesaleShop> findByBusinessIdAndShopName(
            Long businessId,
            String shopName
    );
    List<WholesaleShop> findByBusinessIdOrderByShopNameAsc(
            Long businessId
    );
    List<WholesaleShop> findByBusinessIdAndActiveTrueOrderByShopNameAsc(
            Long businessId
    );
}