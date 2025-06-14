package org.stockify.dto.request.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientRequest {
    @NotBlank
    @Size(max = 20, message = "Max length for first name is 20 characters")
    private String firstName;

    @NotBlank
    @Size(max = 20, message = "Max length for last name is 20 characters")
    private String lastName;

    @NotBlank
    @Size(max = 8)
    private String dni;

    @NotBlank
    @Email(message = "Email format not valid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank
    @Size (message = "Phone number must be between 7 and 20 characters", min = 7, max = 20)
    private String phone;
}