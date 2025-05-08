package org.stockify.model.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;


public record CategoryRequest(
        @NotNull
        @Size(min = 1, max = 60)
        String name
)
{}