package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Long> {
}
