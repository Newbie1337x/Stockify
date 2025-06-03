package org.stockify.dto.response;

import java.util.Set;

public record ProductStoreResponse(
        long productID,
        double price,
        double stock,
        String sku,
        String barcode,
        String brand,
        String description,
        String name,
        Set<String> categories,
        Set<Long> providers
) {
    public ProductStoreResponse {
        if (categories == null) categories = Set.of();
        if (providers == null) providers = Set.of();
    }

}
