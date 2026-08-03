package com.buxbuddy.auth.repository;
import com.buxbuddy.auth.entity.WholesalePartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WholesalePartnerRepository
        extends JpaRepository<WholesalePartner, Long> {

    List<WholesalePartner> findByBusinessIdAndActiveTrue(Long businessId);

    Optional<WholesalePartner> findByIdAndBusinessId(
            Long id,
            Long businessId
    );

    boolean existsByBusinessIdAndName(
            Long businessId,
            String name
    );
}