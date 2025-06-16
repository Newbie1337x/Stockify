package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.SaleEntity;

public class SaleAuditSpecification {

    public static Specification<SaleEntity> byRevision(Long revision) {
        return (root, query, criteriaBuilder) ->
                revision == null ? null : criteriaBuilder.equal(root.get("revision"), revision);
    }

    public static Specification<SaleEntity> byRevisionType(String revisionType) {
        return (root, query, criteriaBuilder) ->
                revisionType == null ? null : criteriaBuilder.equal(root.get("revisionType"), revisionType);
    }

    public static Specification<SaleEntity> bySaleId(Long saleId) {
        return (root, query, criteriaBuilder) ->
                saleId == null ? null : criteriaBuilder.equal(root.get("id"), saleId);
    }

    public static Specification<SaleEntity> byTransactionId(Long transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null ? null : criteriaBuilder.equal(root.get("transaction").get("id"), transactionId);
    }

    public static Specification<SaleEntity> byClientId(Long clientId) {
        return (root, query, criteriaBuilder) ->
                clientId == null ? null : criteriaBuilder.equal(root.get("client").get("id"), clientId);
    }
}