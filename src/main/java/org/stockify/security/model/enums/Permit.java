package org.stockify.security.model.enums;

import lombok.Getter;

@Getter
public enum Permit {
    READ("Read", "Permission to read data"),
    WRITE("Write", "Permission to create and update data"),
    DELETE("Delete", "Permission to delete data"),
    ADMIN("Admin", "Administrative permissions"),
    MANAGE_USERS("Manage Users", "Permission to manage user accounts"),
    MANAGE_ROLES("Manage Roles", "Permission to manage roles and permissions"),
    GENERATE_REPORTS("Generate Reports", "Permission to generate and view reports");

    private final String name;
    private final String description;

    Permit(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
