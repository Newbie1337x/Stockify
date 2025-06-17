package org.stockify.security.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "RoleAndPermitsDTO", description = "DTO that contains a role and its associated set of permissions.")
public class RoleAndPermitsDTO {

    @Schema(description = "Role assigned to the user.", example = "ADMIN")
    private Role roles;

    @Schema(description = "Set of permissions associated with the role.")
    private Set<Permit> permits;
}
