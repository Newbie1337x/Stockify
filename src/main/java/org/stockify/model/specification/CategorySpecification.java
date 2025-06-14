package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.CategoryEntity;

public class CategorySpecification {

    public static Specification<CategoryEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
