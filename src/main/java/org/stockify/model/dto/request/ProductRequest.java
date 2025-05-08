package org.stockify.model.dto.request;

import java.math.BigDecimal;

public record ProductRequest(

        String name,
        String description,
        BigDecimal price,
        BigDecimal stockQuantity
) {}
