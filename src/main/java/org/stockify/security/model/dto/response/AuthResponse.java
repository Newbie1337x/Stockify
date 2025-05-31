package org.stockify.security.model.dto.response;

public record AuthResponse(
    String token
) {
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;



        public AuthResponse build() {
            return new AuthResponse(token);
        }
    }
}
