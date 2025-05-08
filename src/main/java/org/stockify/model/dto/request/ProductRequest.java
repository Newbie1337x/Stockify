package org.stockify.model.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRequest(
        @Size(min = 1, max = 50)
        @NotNull
        String name,
        String description,
        BigDecimal price,
        BigDecimal stock,
        Set<String> categories
) {
        public ProductRequest {
                if (price == null) price = BigDecimal.ZERO;
                if (stock == null) stock = BigDecimal.ZERO;
                if (categories == null) categories = Set.of();
        }
}


