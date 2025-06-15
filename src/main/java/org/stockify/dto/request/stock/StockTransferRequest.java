package org.stockify.dto.request.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockTransferRequest(
        @Schema(description = "ID of the product to transfer", example = "1")
        @NotNull
        Long productId,

        @Schema(description = "ID of the destination store", example = "2")
        @NotNull
        Long destinationStoreId,

        @Schema(description = "Quantity to transfer, must be positive", example = "10.0")
        @NotNull
        @Positive
        Double quantity
) {}
