package org.stockify.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BulkProviderResponse {
    private List<ProviderResponse> results;
    private List<String> errors;

    public BulkProviderResponse(List<ProviderResponse> results, List<String> errors) {
        this.results = results;
        this.errors = errors;
    }
}
