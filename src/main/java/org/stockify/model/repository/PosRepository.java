package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

import java.util.Optional;

/**
 * Repository interface for POS (Point of Sale) entities.
 * Provides methods to query and manage POS terminals in the database.
 */
@Repository
public interface PosRepository extends JpaRepository<PosEntity,Long>, JpaSpecificationExecutor<PosEntity> {

    /**
     * Finds POS terminals by their status with pagination support.
     *
     * @param status The status to filter by (ONLINE, OFFLINE)
     * @param pageable Pagination information
     * @return A page of POS entities matching the status
     */
    Page<PosEntity> findByStatus(Status status, Pageable pageable);

    /**
     * Checks if a POS terminal with the given ID exists.
     *
     * @param posId The ID of the POS terminal to check
     * @return true if a POS with the given ID exists, false otherwise
     */
    boolean existsById(Long posId);

    /**
     * Finds a POS terminal associated with a specific employee.
     *
     * @param employeeId The ID of the employee
     * @return An Optional containing the POS entity if found, or empty if not found
     */
    Optional<PosEntity> findByEmployeeId(Long employeeId);
}
