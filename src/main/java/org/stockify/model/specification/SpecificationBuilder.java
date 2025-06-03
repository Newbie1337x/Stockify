package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {
    private Specification<T> spec = Specification.where(null);

    public SpecificationBuilder<T> add(Specification<T> toAdd) {
        if (toAdd != null) {
            spec = spec == null ? toAdd : spec.and(toAdd);
        }
        return this;
    }

    public Specification<T> build() {
        return spec;
    }
}
