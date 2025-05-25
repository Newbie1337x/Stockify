package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.TimeLogEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeLogSpecifications {

    public static Specification<TimeLogEntity> hasDate(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.equal(root.get("date"), date);
    }

    public static Specification<TimeLogEntity> hasClockInTime(LocalTime clockInTime) {
        return (root, query, cb) -> clockInTime == null ? null : cb.equal(root.get("clockInTime"), clockInTime);
    }

    public static Specification<TimeLogEntity> hasClockOutTime(LocalTime clockOutTime) {
        return (root, query, cb) -> clockOutTime == null ? null : cb.equal(root.get("clockOutTime"), clockOutTime);
    }

    public static Specification<TimeLogEntity> hasEmployeeId(Long employeeId) {
        return (root, query, cb) -> employeeId == null ? null : cb.equal(root.get("employee").get("id"), employeeId);
    }
}
