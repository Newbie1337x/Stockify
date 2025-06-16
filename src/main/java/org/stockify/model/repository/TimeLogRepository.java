package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.TimeLogEntity;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLogEntity, Long>, JpaSpecificationExecutor<TimeLogEntity> {
}
