package org.stockify.security.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "DTO returned after successful authentication, containing the JWT token.")
public record AuthResponse(

        @Schema(description = "JWT token issued for the authenticated session.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token

) {
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token);
        }
    }
}
