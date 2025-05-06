package org.stockify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Integer> {
}
