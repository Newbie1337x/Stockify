package org.stockify.model.dto.response;

import lombok.Data;

@Data
public class BulkItemResponse {
    private String name;
    private String status;    // CREATED, SKIPPED, ERROR
    private String message;   // opcional

    public BulkItemResponse(String name, String status, String message) {
        this.name = name;
        this.status = status;
        this.message = message;
    }
}