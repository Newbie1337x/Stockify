package org.stockify.dto.request.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreRequest(
        @Schema(description = "Name of the store", example = "Main Street Store")
        @NotBlank(message = "Store name is required")
        @Size(max = 100, message = "Store name must be less than 100 characters")
        String storeName,

        @Schema(description = "Store address", example = "123 Main St")
        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must be less than 255 characters")
        String address,

        @Schema(description = "City where the store is located", example = "New York")
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be less than 100 characters")
        String city
) {}
