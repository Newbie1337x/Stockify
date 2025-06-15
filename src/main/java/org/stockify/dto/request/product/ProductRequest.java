package org.stockify.dto.request.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Set;

@Builder
public record ProductRequest(
        @Size(min = 1, max = 50)
        @NotNull
        String name,
        String description,
        BigDecimal price,
        BigDecimal unitPrice,
        BigDecimal stock,
        String sku,
        String barcode,
        String brand,
        Set<String> categories) {

        public ProductRequest {
           if (categories == null) categories = Set.of();
        }
}