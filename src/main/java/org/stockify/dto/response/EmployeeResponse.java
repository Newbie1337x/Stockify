package org.stockify.dto.response;

import lombok.*;
import org.stockify.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for employee response information.
 * Contains all the employee details returned to clients.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "EmployeeResponse", description = "Contains employee information returned in API responses")
public class EmployeeResponse {

    @Schema(description = "Unique identifier of the employee", example = "42")
    private Long id;

    @Schema(description = "Employee's first name", example = "Alice")
    private String name;

    @Schema(description = "Employee's last name", example = "Smith")
    private String lastName;

    @Schema(description = "Employee's DNI (national identity document number)", example = "12345678")
    private String dni;

    @Schema(description = "Current status of the employee", example = "ONLINE")
    private Status status;

    @Schema(description = "Indicates if the employee is active", example = "true")
    private Boolean active;
}
