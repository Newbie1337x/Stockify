package org.stockify.dto.request.stock;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockTransferRequest(
        @NotNull Long productId,
        @NotNull Long destinationStoreId,
        @NotNull @Positive Double quantity
) {}
