package org.stockify.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(name = "ClientRequest", description = "Request body for creating or updating a client.")
public class UserRequest {




    @Schema(description = "Client's image", example = "http//:image.com", maxLength = 20)
    private String img;


    @NotBlank
    @Size(max = 20, message = "Max length for first name is 20 characters")
    @Schema(description = "Client's first name", example = "María", maxLength = 20)
    private String firstName;

    @NotBlank
    @Size(max = 20, message = "Max length for last name is 20 characters")
    @Schema(description = "Client's last name", example = "Rodríguez", maxLength = 20)
    private String lastName;

    @NotBlank
    @Size(max = 8)
    @Schema(description = "Client's DNI (National ID Number)", example = "33445566", maxLength = 8)
    private String dni;

    @NotBlank
    @Email(message = "Email format not valid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Schema(description = "Client's email address", example = "maria.rodriguez@example.com", format = "email")
    private String email;

    @NotBlank
    @Size(min = 7, max = 20, message = "Phone number must be between 7 and 20 characters")
    @Schema(description = "Client's phone number", example = "+54 9 11 6543-9876", minLength = 7, maxLength = 20)
    private String phone;
}
