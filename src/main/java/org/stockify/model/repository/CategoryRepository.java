package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stockify.model.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {

    boolean existsByName(String name);
}
