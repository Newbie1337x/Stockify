package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.SessionPosEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionPosSpecifications {
    
    public static Specification<SessionPosEntity> hasEmployeeId(Long employeeId) {
        return (root, query, cb) ->
                employeeId == null ? null : cb.equal(root.get("employee").get("id"), employeeId);
    }
    
    public static Specification<SessionPosEntity> hasPosId(Long posId) {
        return (root, query, cb) ->
                posId == null ? null : cb.equal(root.get("posEntity").get("id"), posId);
    }
    
    public static Specification<SessionPosEntity> hasOpeningTimeAfter(LocalDateTime startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("openingTime"), startDate);
    }
    
    public static Specification<SessionPosEntity> hasOpeningTimeBefore(LocalDateTime endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("openingTime"), endDate);
    }
    
    public static Specification<SessionPosEntity> hasCloseTimeAfter(LocalDateTime startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("closeTime"), startDate);
    }
    
    public static Specification<SessionPosEntity> hasCloseTimeBefore(LocalDateTime endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("closeTime"), endDate);
    }
    
    public static Specification<SessionPosEntity> hasOpeningAmountGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.greaterThanOrEqualTo(root.get("openingAmount"), amount);
    }
    
    public static Specification<SessionPosEntity> hasOpeningAmountLessThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.lessThanOrEqualTo(root.get("openingAmount"), amount);
    }
    
    public static Specification<SessionPosEntity> hasCloseAmountGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.greaterThanOrEqualTo(root.get("closeAmount"), amount);
    }
    
    public static Specification<SessionPosEntity> hasCloseAmountLessThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.lessThanOrEqualTo(root.get("closeAmount"), amount);
    }
    
    public static Specification<SessionPosEntity> hasCashDifferenceGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.greaterThanOrEqualTo(root.get("cashDifference"), amount);
    }
    
    public static Specification<SessionPosEntity> hasCashDifferenceLessThan(BigDecimal amount) {
        return (root, query, cb) ->
                amount == null ? null : cb.lessThanOrEqualTo(root.get("cashDifference"), amount);
    }
    
    public static Specification<SessionPosEntity> isOpen(Boolean isOpen) {
        return (root, query, cb) ->
                isOpen == null ? null : isOpen ? cb.isNull(root.get("closeTime")) : cb.isNotNull(root.get("closeTime"));
    }
}