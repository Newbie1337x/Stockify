package org.stockify.security.model.dto.request;

import lombok.Builder;

@Builder
public record AuthRequest(String username, String password) {
}
