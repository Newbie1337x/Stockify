package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity,Long>, JpaSpecificationExecutor<SaleEntity> {
}
