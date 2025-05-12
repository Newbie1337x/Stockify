package org.stockify.dto.response;
import java.util.Set;

public record ProductResponse(
        int id,
        String name,
        double price,
        double stock,
        Set<String> categories
) {
    public ProductResponse {
        if (categories == null) categories = Set.of();
    }
}
