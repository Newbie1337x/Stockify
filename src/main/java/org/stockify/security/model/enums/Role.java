package org.stockify.security.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Administrator", "Full system access"),
    MANAGER("Manager", "Department management access"),
    EMPLOYEE("Employee", "Basic system access");


    private final String name;
    private final String description;

    Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
