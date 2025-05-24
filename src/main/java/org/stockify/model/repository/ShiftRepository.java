package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ShiftEntity;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {
    /// LOS DE EMPLEADOS TAMBIÉN O NO ¡?¡??¡
    ShiftEntity findByEmployeeId(Long employeeId);
    ShiftEntity findByEmployeeIdAndDate(Long employeeId, String date);
    ShiftEntity findByDate(String date);
    Page<ShiftEntity> findAll(Pageable pageable);
    Page<ShiftEntity> findByDate(String date, Pageable pageable);
    Optional<ShiftEntity> findById(Long id);
}