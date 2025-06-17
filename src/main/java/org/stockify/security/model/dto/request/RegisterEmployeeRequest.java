package org.stockify.security.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.stockify.dto.request.employee.EmployeeRequest;

/**
 * Request body for registering a new employee with both personal and credential information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(name = "RegisterEmployeeRequest", description = "Request body for registering a new employee with both personal and credential information.")
public class RegisterEmployeeRequest {

    @Schema(description = "Employee personal and employment information.")
    private EmployeeRequest employee;

    @Schema(description = "Credential information needed to access the system.")
    private CredentialRequest credential;
}
