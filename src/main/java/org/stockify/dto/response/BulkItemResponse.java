package org.stockify.dto.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class BulkItemResponse {

    @Schema(description = "Name of the item", example = "Product A")
    private String name;

    @Schema(description = "Status of the item processing", example = "CREATED", allowableValues = {"CREATED", "SKIPPED", "ERROR"})
    private String status;

    @Schema(description = "Optional message related to the item processing", example = "Item already exists")
    private String message;

    public BulkItemResponse(String name, String status, String message) {
        this.name = name;
        this.status = status;
        this.message = message;
    }
}
