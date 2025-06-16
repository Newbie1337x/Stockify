package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
public class BulkProviderResponse {

    @Schema(description = "List of successfully processed providers")
    private List<ProviderResponse> results;

    @Schema(description = "List of error messages encountered during processing")
    private List<String> errors;

    public BulkProviderResponse(List<ProviderResponse> results, List<String> errors) {
        this.results = results;
        this.errors = errors;
    }
}
