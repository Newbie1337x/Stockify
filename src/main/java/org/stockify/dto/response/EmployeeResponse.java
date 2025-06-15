package org.stockify.dto.response;

import lombok.*;
import org.stockify.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeResponse {

    @Schema(description = "Unique identifier of the employee", example = "42")
    private Long id;

    @Schema(description = "Employee's first name", example = "Alice")
    private String name;

    @Schema(description = "Employee's last name", example = "Smith")
    private String lastName;

    @Schema(description = "Employee's DNI (national identity document number)", example = "12345678")
    private String dni;

    @Schema(description = "Current status of the employee", example = "ACTIVE")
    private Status status;

    @Schema(description = "Indicates if the employee is active", example = "true")
    private Boolean active;
}
