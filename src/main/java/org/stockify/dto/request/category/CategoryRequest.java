package org.stockify.dto.request.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CategoryRequest", description = "Request body for creating or updating a category.")
public class CategoryRequest {

    @NotNull
    @Size(min = 1, max = 60)
    @Schema(
            description = "Name of the category",
            example = "Electronics",
            minLength = 1,
            maxLength = 60,
            required = true
    )
    private String name;
}
