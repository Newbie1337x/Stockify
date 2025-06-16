package org.stockify.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.StockEntity;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long>, JpaSpecificationExecutor<StockEntity> {


    Optional<StockEntity> findByProductIdAndStoreId(Long productId, Long storeId);

    void deleteByProductIdAndStoreId(Long productId, Long storeId);
}
