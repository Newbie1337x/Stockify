package org.stockify.dto.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
public class BulkProductResponse {

    @Schema(description = "Total number of requested items", example = "100")
    private int totalRequested;

    @Schema(description = "Total number of items successfully created", example = "80")
    private int totalCreated;

    @Schema(description = "Total number of items skipped", example = "10")
    private int totalSkipped;

    @Schema(description = "Total number of items with errors", example = "10")
    private int totalError;

    @Schema(description = "List of individual item processing results")
    private List<BulkItemResponse> results;

    public BulkProductResponse(int totalRequested, int totalCreated, int totalSkipped, int totalError, List<BulkItemResponse> results) {
        this.totalRequested = totalRequested;
        this.totalCreated = totalCreated;
        this.totalSkipped = totalSkipped;
        this.totalError = totalError;
        this.results = results;
    }
}
