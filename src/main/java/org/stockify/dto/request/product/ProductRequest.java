package org.stockify.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Set;

@Builder
public record ProductRequest(
        @Schema(description = "Name of the product", example = "Monitor 144hz", minLength = 1, maxLength = 50)
        @Size(min = 1, max = 50)
        @NotNull
        String name,

        @Schema(description = "Detailed description of the product", example = "Latest model with advanced features")
        String description,

        @Schema(description = "Price of the product", example = "999.99")
        BigDecimal price,

        @Schema(description = "Unit price of the product", example = "899.99")
        BigDecimal unitPrice,

        @Schema(description = "Available stock quantity", example = "100")
        BigDecimal stock,

        @Schema(description = "SKU code (Stock Keeping Unit)", example = "PHONE-001")
        String sku,

        @Schema(description = "Barcode of the product", example = "0123456789012")
        String barcode,

        @Schema(description = "Brand name of the product", example = "TechBrand")
        String brand,

        @Schema(description = "Set of categories the product belongs to", example = "[\"Electronics\", \"Mobile\"]")
        Set<String> categories
) {
    public ProductRequest {
        if (categories == null) categories = Set.of();
    }
}
