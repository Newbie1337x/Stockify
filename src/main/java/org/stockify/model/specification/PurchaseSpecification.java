package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.PurchaseEntity;

public class PurchaseSpecification {

    public static Specification<PurchaseEntity> ByTransactionId(Long transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null ? null : criteriaBuilder.equal(root.get("transaction").get("id"), transactionId);
    }

    public static Specification<PurchaseEntity> ByProviderId(Long providerId) {
        return (root, query, criteriaBuilder) ->
                providerId == null ? null : criteriaBuilder.equal(root.get("provider").get("id"), providerId);
    }

    public static Specification<PurchaseEntity> ByPurchaseId(Long purchaseId) {
        return (root, query, criteriaBuilder) ->
                purchaseId == null ? null : criteriaBuilder.equal(root.get("id"), purchaseId);
    }
}
