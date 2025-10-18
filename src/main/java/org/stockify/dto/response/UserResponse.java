package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse extends RepresentationModel<UserResponse> {

    @Schema(description = "Unique identifier of the client", example = "123")
    private Long id;

    @Schema(description = "Client's first name", example = "John")
    private String firstName;

    @Schema(description = "Client's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Client's DNI (national identity document number)", example = "12345678")
    private String dni;

    @Schema(description = "Client's phone number", example = "+541112345678")
    private String phone;

    @Schema(description = "Client's image", example = "http//:image.com", maxLength = 20)
    private String img;
}
