package org.stockify.model.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.ShiftEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ShiftSpecification {
    public static Specification<ShiftEntity> dayBetween (LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("day"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("day"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("day"), end);
            }
            return null;
        };
    }

    public static Specification<ShiftEntity> entryTimeBetween (LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("entryTime"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("entryTime"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("entryTime"), end);
            }
            return null;
        };
    }

    public static Specification<ShiftEntity> exitTimeBetween (LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("exitTime"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("exitTime"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("exitTime"), end);
            }
            return null;
        };
    }

    public static Specification<ShiftEntity> employeeDni(String dni) {
        return (root, query, criteriaBuilder) -> {
            if (dni != null && !dni.isEmpty()) {
                Join<ShiftEntity, EmployeeEntity> employeeJoin = root.join("employee");
                return criteriaBuilder.equal(employeeJoin.get("dni"), dni);
            }
            return null;
        };
    }


}
