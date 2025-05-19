package org.stockify.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;

@Getter
public class AssignProductRequest {
    @NotNull
    @NotEmpty
    Set<Integer> productsId;
}
