package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long>, JpaSpecificationExecutor<StockEntity> {

    Page<StockEntity> findByStoreId(Long storeId, Pageable pageable);

    Optional<StockEntity> findByProductIdAndStoreId(Long productId, Long storeId);

    void deleteByProductIdAndStoreId(Long productId, Long storeId);

    Page<StockEntity> findByStoreIdAndProductIn(Long storeId, List<ProductEntity> products, Pageable pageable);
}
