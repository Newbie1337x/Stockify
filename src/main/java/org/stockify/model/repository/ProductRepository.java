package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

}
