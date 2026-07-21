package com.buxbuddy.auth.repository;
import com.buxbuddy.auth.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository
        extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);

}
