package org.stockify.security.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AuthRequest", description = "DTO used for user authentication containing email and password.")
public record AuthRequest(

        @Schema(description = "User email address used to authenticate.", example = "user@example.com")
        String email,

        @Schema(description = "User password in plain text (must be encrypted before storage).", example = "P@ssw0rd")
        String password

) {}
