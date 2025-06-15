package org.stockify.dto.request.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.stockify.model.enums.Status;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "EmployeeRequest", description = "Request body for creating or updating an employee.")
public class EmployeeRequest implements Serializable {

    @NotBlank
    @Schema(description = "Employee's first name", example = "Juan")
    private String name;

    @NotBlank
    @Size(min = 7, max = 8)
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    @Schema(description = "Employee's DNI (digits only, 7 to 8 characters)", example = "12345678", minLength = 7, maxLength = 8, pattern = "^\\d{7,8}$")
    private String dni;

    @NotBlank
    @Schema(description = "Employee's last name", example = "Pérez")
    private String lastName;

    @NotNull
    @Schema(description = "Current status of the employee", example = "ONLINE")
    private Status status;

    @NotNull
    @Schema(description = "Indicates if the employee is active", example = "true")
    private Boolean active;
}
