package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.PurchaseEntity;

public class PurchaseAuditSpecification {

    public static Specification<PurchaseEntity> byRevision(Long revision) {
        return (root, query, criteriaBuilder) ->
                revision == null ? null : criteriaBuilder.equal(root.get("revision"), revision);
    }

    public static Specification<PurchaseEntity> byRevisionType(String revisionType) {
        return (root, query, criteriaBuilder) ->
                revisionType == null ? null : criteriaBuilder.equal(root.get("revisionType"), revisionType);
    }

    public static Specification<PurchaseEntity> byPurchaseId(Long purchaseId) {
        return (root, query, criteriaBuilder) ->
                purchaseId == null ? null : criteriaBuilder.equal(root.get("id"), purchaseId);
    }

    public static Specification<PurchaseEntity> byTransactionId(Long transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null ? null : criteriaBuilder.equal(root.get("transaction").get("id"), transactionId);
    }

    public static Specification<PurchaseEntity> byProviderId(Long providerId) {
        return (root, query, criteriaBuilder) ->
                providerId == null ? null : criteriaBuilder.equal(root.get("provider").get("id"), providerId);
    }
}