package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stockify.model.entity.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {


    Optional<CategoryEntity> findByName(String categoryName);
}
