package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

import java.math.BigDecimal;

public class PosSpecification {

    public static Specification<PosEntity> byId(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<PosEntity> byStoreId(Long storeId) {
        return (root, query, criteriaBuilder) ->
                storeId == null ? null : criteriaBuilder.equal(root.get("store").get("id"), storeId);
    }

    public static Specification<PosEntity> byStatus(Status status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<PosEntity> byEmployeeId(Long employeeId) {
        return (root, query, criteriaBuilder) ->
                employeeId == null ? null : criteriaBuilder.equal(root.get("employee").get("id"), employeeId);
    }

    public static Specification<PosEntity> byCurrentAmountGreaterThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("currentAmount"), amount);
    }

    public static Specification<PosEntity> byCurrentAmountLessThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("currentAmount"), amount);
    }
}
