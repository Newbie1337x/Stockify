package org.stockify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Integer> {
}
