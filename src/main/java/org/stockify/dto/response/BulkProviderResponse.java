package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkProviderResponse {
    private List<ProviderResponse> results;
    private List<String> errors;

    public BulkProviderResponse(List<ProviderResponse> results, List<String> errors) {
        this.results = results;
        this.errors = errors;
    }
}
