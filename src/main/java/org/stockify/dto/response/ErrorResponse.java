package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "Error response DTO")
public record ErrorResponse(
        @Schema(description = "Error message describing what went wrong", example = "Resource not found")
        String message,

        @Schema(description = "Type or category of the error", example = "NotFoundException")
        String error,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Request path where the error occurred", example = "/api/products/1")
        String path,

        @Schema(description = "Timestamp when the error occurred", example = "2025-06-15T14:30:00")
        LocalDateTime timestamp
){}
