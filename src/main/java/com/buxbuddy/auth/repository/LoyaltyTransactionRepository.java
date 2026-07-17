package com.buxbuddy.auth.repository;

import com.buxbuddy.auth.entity.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTransactionRepository
        extends JpaRepository<LoyaltyTransaction, Long> {

}