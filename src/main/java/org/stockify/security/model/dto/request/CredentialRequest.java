package org.stockify.security.model.dto.request;

import lombok.*;
import org.stockify.security.model.entity.RoleEntity;
import org.stockify.security.model.enums.Permit;
import org.stockify.security.model.enums.Role;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialRequest {
    private String username;
    private String password;
    private String email;
    private Role roles;
    private Set<Permit> permits;
}
