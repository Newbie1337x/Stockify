package org.stockify.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class BulkProductResponse{
    private int totalRequested;
    private int totalCreated;
    private int totalSkipped;
    private int totalError;
    private List<BulkItemResponse> results;

    public BulkProductResponse(int size, int created, int skipped, int error, List<BulkItemResponse> results) {
        this.totalRequested = size;
        this.totalCreated = created;
        this.totalSkipped = skipped;
        this.totalError = error;
        this.results = results;
    }
}
