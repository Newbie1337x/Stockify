package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLogEntity, Long>, JpaSpecificationExecutor<TimeLogEntity> {
    List<TimeLogEntity> findByDate(LocalDate date);
    List<TimeLogEntity> findByClockInTime(LocalTime clockInTime);
    List<TimeLogEntity> findByClockOutTime(LocalTime clockOutTime);
}
