package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stock information of a product in a specific store")
public record StockResponse(
        @Schema(description = "ID of the product", example = "1001")
        Long product_id,

        @Schema(description = "ID of the store", example = "10")
        Long store_id,

        @Schema(description = "Available stock quantity", example = "150.0")
        Double stock
) {}
