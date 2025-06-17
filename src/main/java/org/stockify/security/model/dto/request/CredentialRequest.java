package org.stockify.security.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.stockify.security.model.enums.Permit;
import org.stockify.security.model.enums.Role;

import java.util.Set;

/**
 * DTO used to receive user credential information during authentication or registration requests.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "CredentialRequest", description = "Request body for user credential operations such as login or signup.")
public class CredentialRequest {

    @Schema(description = "Unique username of the user.", example = "john_doe")
    private String username;

    @Schema(description = "User password (must be encrypted before storing).", example = "P@ssw0rd123")
    private String password;

    @Schema(description = "User email address used for communication or account recovery.", example = "john@example.com")
    private String email;

    @Schema(description = "Role assigned to the user determining access level.", example = "ADMIN")
    private Role roles;

    @Schema(description = "Set of fine-grained permissions assigned to the user.")
    private Set<Permit> permits;
}
