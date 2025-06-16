package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

@Repository
public interface PosRepository extends JpaRepository<PosEntity,Long>, JpaSpecificationExecutor<PosEntity> {

    Page<PosEntity> findByStatus(Status status, Pageable pageable);
    boolean existsById(Long posId);
}
