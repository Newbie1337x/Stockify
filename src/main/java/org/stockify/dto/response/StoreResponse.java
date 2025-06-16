package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Store information")
public record StoreResponse(
        @Schema(description = "Unique identifier of the store", example = "1")
        Long id,

        @Schema(description = "Name of the store", example = "Main Street Store")
        String storeName,

        @Schema(description = "Store address", example = "123 Main St, Downtown")
        String address,

        @Schema(description = "City where the store is located", example = "New York")
        String city
) {}
