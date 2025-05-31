package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.EmployeeEntity;

public class EmployeeSpecifications {
    public static Specification<EmployeeEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> hasDni(String dni) {
        return (root, query, cb) ->
                dni == null ? null : cb.equal(root.get("dni"), dni);
    }

    public static Specification<EmployeeEntity> hasStatus(Enum status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<EmployeeEntity> isActive() {
        return (root, query, cb) ->
                cb.isTrue(root.get("active"));
    }
}
