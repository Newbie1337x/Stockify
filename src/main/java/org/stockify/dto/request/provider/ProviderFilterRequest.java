package org.stockify.dto.request.provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderFilterRequest {

    @Schema(description = "Provider's name or contact person", example = "John Supplier")
    private String name;

    @Schema(description = "Business name of the provider", example = "Electronics Supplier Inc.")
    private String businessName;

    @Schema(description = "Tax identification number", example = "12345678")
    private String taxId;

    @Schema(description = "Email address of the provider", example = "contact@electronics.com")
    private String email;

    @Schema(description = "Contact phone number", example = "555-1234")
    private String phone;

    @Schema(description = "Active status of the provider, e.g., true or false", example = "true")
    private String active;

    @Schema(description = "Unique identifier of the provider", example = "1")
    private Long id;

    @Schema(description = "Tax address of the provider", example = "100 Supplier St")
    private String taxAddress;
}
