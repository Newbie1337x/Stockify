package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category response DTO")
public record CategoryResponse(
        @Schema(description = "Unique identifier of the category", example = "1")
        int id,

        @Schema(description = "Name of the category", example = "Electronics")
        String name
) {}
