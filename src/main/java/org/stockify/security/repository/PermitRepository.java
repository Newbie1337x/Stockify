package org.stockify.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.security.model.entity.PermitEntity;
import org.stockify.security.model.enums.Permit;

import java.util.Optional;

@Repository
public interface PermitRepository extends JpaRepository<PermitEntity, Long> {
    Optional<PermitEntity> findByPermit(Permit permit);
} 