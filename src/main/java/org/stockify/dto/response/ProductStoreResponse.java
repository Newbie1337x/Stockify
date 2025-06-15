package org.stockify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Product information with store-specific details")
public record ProductStoreResponse(
        @Schema(description = "Unique identifier of the product", example = "101")
        long productID,

        @Schema(description = "Price of the product in the store", example = "19.99")
        double price,

        @Schema(description = "Available stock quantity in the store", example = "50.0")
        double stock,

        @Schema(description = "SKU (Stock Keeping Unit) code", example = "SKU12345")
        String sku,

        @Schema(description = "Barcode of the product", example = "0123456789012")
        String barcode,

        @Schema(description = "Brand name of the product", example = "Acme")
        String brand,

        @Schema(description = "Product description", example = "High-quality wireless headphones")
        String description,

        @Schema(description = "Product name", example = "Wireless Headphones")
        String name,

        @Schema(description = "Categories this product belongs to")
        Set<String> categories,

        @Schema(description = "IDs of providers associated with the product")
        Set<Long> providers
) {
    public ProductStoreResponse {
        if (categories == null) categories = Set.of();
        if (providers == null) providers = Set.of();
    }
}
