package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.WholesaleSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WholesaleSaleItemRepository
        extends JpaRepository<WholesaleSaleItem, Long> {

    List<WholesaleSaleItem>
    findBySaleBusinessIdAndWholesaleProductId(
            Long businessId,
            Long wholesaleProductId
    );

    List<WholesaleSaleItem>
    findBySaleId(Long saleId);

    List<WholesaleSaleItem>
    findByWholesaleProductId(Long wholesaleProductId);
}