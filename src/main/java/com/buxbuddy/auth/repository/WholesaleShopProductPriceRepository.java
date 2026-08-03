package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.WholesaleShopProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleShopProductPriceRepository
        extends JpaRepository<WholesaleShopProductPrice, Long> {

    boolean existsByShopIdAndWholesaleProductId(
            Long shopId,
            Long wholesaleProductId
    );

    Optional<WholesaleShopProductPrice>
    findByShopIdAndWholesaleProductId(
            Long shopId,
            Long wholesaleProductId
    );

    List<WholesaleShopProductPrice>
    findByShopIdOrderByIdDesc(
            Long shopId
    );

    List<WholesaleShopProductPrice>
    findByWholesaleProductIdOrderByIdDesc(
            Long wholesaleProductId
    );

    List<WholesaleShopProductPrice>
    findByShopIdAndActiveTrueOrderByIdDesc(
            Long shopId
    );
}