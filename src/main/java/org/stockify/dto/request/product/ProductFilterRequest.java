package org.stockify.dto.request.product;

import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class ProductFilterRequest {

    @Schema(description = "Exact price to filter", example = "99.99", nullable = true)
    private Double price;

    @Schema(description = "Exact stock quantity to filter", example = "100", nullable = true)
    private Double stock;

    @Schema(description = "Filter for stock greater than this value", example = "50", nullable = true)
    private Double stockGreaterThan;

    @Schema(description = "Filter for stock less than this value", example = "200", nullable = true)
    private Double stockLessThan;

    @Schema(description = "Filter for stock between two values (min and max)", example = "[50, 150]", nullable = true)
    @Size(min = 2, max = 2, message = "stockBetween needs exactly 2 parameters")
    private List<Double> stockBetween;

    @Schema(description = "Filter by product name", example = "Smartphone", nullable = true)
    private String name;

    @Schema(description = "Filter by product description", example = "Latest model with 5G", nullable = true)
    private String description;

    @Schema(description = "Filter by product barcode", example = "BARCODE-001", nullable = true)
    private String barcode;

    @Schema(description = "Filter by SKU (Stock Keeping Unit)", example = "PHONE-001", nullable = true)
    private String sku;

    @Schema(description = "Filter by product brand", example = "TechBrand", nullable = true)
    private String brand;

    @Schema(description = "Filter by provider name", example = "Electronics Supplier Inc.", nullable = true)
    private String provider;

    @Schema(description = "Filter by category name", example = "Electronics", nullable = true)
    private String category;

    @Schema(description = "Filter by multiple categories", example = "[\"Electronics\", \"Home\"]", nullable = true)
    private List<String> categories;

    @Schema(description = "Filter by multiple providers", example = "[\"Electronics Supplier Inc.\", \"Clothing Wholesale Ltd.\"]", nullable = true)
    private List<String> providers;

    @Schema(description = "Filter for price between two values (min and max)", example = "[50.00, 150.00]", nullable = true)
    @Size(min = 2, max = 2, message = "priceBetween needs exactly 2 parameters")
    private List<Double> priceBetween;

    @Schema(description = "Filter for price greater than this value", example = "100.00", nullable = true)
    private Double priceGreater;

    @Schema(description = "Filter for price less than this value", example = "500.00", nullable = true)
    private Double priceLess;
}
