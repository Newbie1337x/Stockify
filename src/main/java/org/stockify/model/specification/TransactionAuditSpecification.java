package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.PaymentMethod;
import org.stockify.model.enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionAuditSpecification {

    public static Specification<TransactionEntity> byRevision(Long revision) {
        return (root, query, criteriaBuilder) ->
                revision == null ? null : criteriaBuilder.equal(root.get("revision"), revision);
    }

    public static Specification<TransactionEntity> byRevisionType(String revisionType) {
        return (root, query, criteriaBuilder) ->
                revisionType == null ? null : criteriaBuilder.equal(root.get("revisionType"), revisionType);
    }

    public static Specification<TransactionEntity> byTransactionId(Long transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null ? null : criteriaBuilder.equal(root.get("id"), transactionId);
    }

    public static Specification<TransactionEntity> byPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, criteriaBuilder) ->
                paymentMethod == null ? null : criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);
    }

    public static Specification<TransactionEntity> byType(TransactionType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<TransactionEntity> byStoreId(Long storeId) {
        return (root, query, criteriaBuilder) ->
                storeId == null ? null : criteriaBuilder.equal(root.get("store").get("id"), storeId);
    }

    public static Specification<TransactionEntity> bySessionPosId(Long sessionPosId) {
        return (root, query, criteriaBuilder) ->
                sessionPosId == null ? null : criteriaBuilder.equal(root.get("sessionPosEntity").get("id"), sessionPosId);
    }

    public static Specification<TransactionEntity> byDateAfter(LocalDateTime fromDate) {
        return (root, query, criteriaBuilder) ->
                fromDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), fromDate);
    }

    public static Specification<TransactionEntity> byDateBefore(LocalDateTime toDate) {
        return (root, query, criteriaBuilder) ->
                toDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dateTime"), toDate);
    }
}