package org.stockify.security.model.dto.request;

import lombok.*;
import org.stockify.security.model.enums.Permit;
import org.stockify.security.model.enums.Role;

import java.util.Set;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleAndPermitsDTO {
    private Role roles;
    private Set<Permit> permits;
}
