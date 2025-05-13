package org.stockify.dto.request;

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
    @Size(min = 5, message = "")
    private String firstName;

    @NotBlank
    @Size(min = 5, message = "")
    private String lastName;

    @NotBlank
    @Size(max = 8)
    private int dni;

    @NotBlank
    @Email(message = "el formato de mail no es valido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 5, message = "")
    private String phone;
}
