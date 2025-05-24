package org.stockify.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;
@Data
public class AssignProvidersRequest {
    @NotNull
    @NotEmpty
    Set<Long> providersIds;
    public AssignProvidersRequest() {
        providersIds = Set.of();
    }
}
