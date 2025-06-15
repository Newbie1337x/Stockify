package org.stockify.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(name = "ClientFilterRequest", description = "Filter parameters for searching clients.")
public class ClientFilterRequest {

    @Schema(description = "Client's first name to filter by", example = "Lucas")
    private String firstName;

    @Schema(description = "Client's last name to filter by", example = "Gómez")
    private String lastName;

    @Schema(description = "Client's DNI (National ID number)", example = "40555444")
    private String dni;

    @Schema(description = "Client's phone number", example = "+54 9 11 5555-1234")
    private String phone;
}
