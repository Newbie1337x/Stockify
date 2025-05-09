package org.stockify.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class ProductRequest {
    @Size(min = 1, max = 50)
    @NotNull
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal stock;
    private Set<String> categories;
}