package org.stockify.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String error,
        int status,
        String path,
        LocalDateTime timestamp
)
{}
