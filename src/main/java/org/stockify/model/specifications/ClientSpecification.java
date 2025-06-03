package org.stockify.model.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entities.ClientEntity;

public class ClientSpecification {
    public static Specification<ClientEntity> lastNameLike(String lastName) {
        return (root, query, criteriaBuilder) ->
                lastName == null ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    /// int ? Integer ? String
    public static Specification<ClientEntity> dniEquals(String dni) {
        return (root, query, criteriaBuilder) ->
                dni == null ? null :
                        criteriaBuilder.equal(root.get("dni"), dni);
    }

    public static Specification<ClientEntity> phoneLike(String phone) {
        return (root, query, criteriaBuilder) ->
                phone == null ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
    }

    public static Specification<ClientEntity> firstNameLike(String firstName) {
        return (root, query, criteriaBuilder) ->
                firstName == null ? null :
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

