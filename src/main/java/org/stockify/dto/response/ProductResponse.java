package org.stockify.dto.response;
import java.util.Set;

public record ProductResponse(
        Long id,
        String name,
        double price,
        String sku,
        String barcode,
        String description,
        String brand,
        Set<String> categories,
        Set<Long> providers,
        Set<StockResponse> stocks
) {
    public ProductResponse {
        if (categories == null) categories = Set.of();
        if (providers == null) providers = Set.of();
        if (stocks == null) stocks = Set.of();
    }
}
