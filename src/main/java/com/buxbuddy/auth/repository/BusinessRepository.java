package com.buxbuddy.auth.repository;



import com.buxbuddy.auth.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByBusinessName(String businessName);
    Optional<Business> findByBusinessNameIgnoreCase(
            String businessName
    );
    @Query("""
            SELECT b
            FROM Business b
            LEFT JOIN FETCH b.businessCategory
            LEFT JOIN FETCH b.province
            WHERE b.id = :id
            """)
    Optional<Business> findByIdWithDetails(Long id);

    @Query("""
            SELECT DISTINCT b
            FROM Business b
            LEFT JOIN FETCH b.businessCategory
            LEFT JOIN FETCH b.province
            """)
    List<Business> findAllWithDetails();

}