package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Product response DTO")
public record ProductResponse(
        @Schema(description = "Unique identifier of the product", example = "1001")
        Long id,

        @Schema(description = "Name of the product", example = "Wireless Mouse")
        String name,

        @Schema(description = "Price of the product", example = "25.99")
        double price,

        @Schema(description = "Unit price of the product", example = "5.20")
        double unitPrice,

        @Schema(description = "SKU (Stock Keeping Unit) code", example = "WM-12345")
        String sku,

        @Schema(description = "Barcode of the product", example = "0123456789012")
        String barcode,

        @Schema(description = "Product description", example = "Ergonomic wireless mouse with USB receiver")
        String description,

        @Schema(description = "Brand of the product", example = "LogiTech")
        String brand,

        @Schema(description = "Categories that the product belongs to")
        Set<String> categories,

        @Schema(description = "Provider IDs associated with the product")
        Set<Long> providers,

        @Schema(description = "Stock details per store or location")
        Set<StockResponse> stocks
) {
    public ProductResponse {
        if (categories == null) categories = Set.of();
        if (providers == null) providers = Set.of();
        if (stocks == null) stocks = Set.of();
    }
}
