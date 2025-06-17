package org.stockify.security.model.dto.request;

import lombok.*;
import org.stockify.dto.request.employee.EmployeeRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegisterEmployeeRequest {
    EmployeeRequest employee;
    CredentialRequest credential;
}
