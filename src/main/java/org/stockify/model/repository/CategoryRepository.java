package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.stockify.model.entity.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {


    boolean existsByNameIgnoreCase(String categoryName);
    Optional<CategoryEntity> findByName(String categoryName);
    Optional<CategoryEntity> findByNameIgnoreCase(String categoryName);
}
