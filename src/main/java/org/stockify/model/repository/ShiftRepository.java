package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ShiftEntity;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Long>, JpaSpecificationExecutor<ShiftEntity> {
    Optional<ShiftEntity> findById(Long id);
    Page<ShiftEntity> findAll(Pageable pageable);
}
