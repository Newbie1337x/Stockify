package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.SaleEntity;

public class SaleSpecification {
    public static Specification<SaleEntity> byTransactionId(Long transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null ? null : criteriaBuilder.equal(root.get("transaction").get("id"), transactionId);
    }

    public static Specification<SaleEntity> byClientId(Long clientId) {
        return (root, query, criteriaBuilder) ->
                clientId == null ? null : criteriaBuilder.equal(root.get("client").get("id"), clientId);
    }

    public static Specification<SaleEntity> bySaleId(Long saleId) {
        return (root, query, criteriaBuilder) ->
                saleId == null ? null : criteriaBuilder.equal(root.get("id"), saleId);
    }
}
