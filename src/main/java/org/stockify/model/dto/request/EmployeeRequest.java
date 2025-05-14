package org.stockify.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.stockify.model.enums.Status;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequest implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotNull
    private Status status;
    @NotNull
    private Boolean active;
}
