package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
}
