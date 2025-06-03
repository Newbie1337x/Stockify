package org.stockify.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockRequest(
    @NotNull(message = "Product ID is required")
    Long productId,
    
    @NotNull(message = "Store ID is required")
    Long storeId,
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    Double quantity
) {}