package org.stockify.security.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response object returned after successful authentication.
 * Contains the JWT token that should be used for subsequent authenticated requests.
 */
@Schema(name = "AuthResponse", description = "DTO returned after successful authentication, containing the JWT token.")
public record AuthResponse(

        @Schema(description = "JWT token issued for the authenticated session.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token

) {
    /**
     * Creates a new builder for AuthResponse
     * 
     * @return A new AuthResponseBuilder instance
     */
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    /**
     * Builder class for creating AuthResponse instances
     */
    public static class AuthResponseBuilder {
        private String token;

        /**
         * Sets the token value for the AuthResponse
         * 
         * @param token The JWT token to include in the response
         * @return This builder for method chaining
         */
        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Builds the AuthResponse with the configured values
         * 
         * @return A new AuthResponse instance
         */
        public AuthResponse build() {
            return new AuthResponse(token);
        }
    }
}
