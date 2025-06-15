package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ProviderResponse {

    @Schema(description = "Unique identifier of the provider", example = "101")
    private Long id;

    @Schema(description = "Business name of the provider", example = "Tech Supplies Ltd.")
    private String businessName;

    @Schema(description = "Tax identification number", example = "20304050607")
    private String taxId;

    @Schema(description = "Tax address of the provider", example = "123 Main St, Springfield")
    private String taxAddress;

    @Schema(description = "Contact phone number", example = "+1-555-1234567")
    private String phone;

    @Schema(description = "Contact email address", example = "contact@techsupplies.com")
    private String email;

    @Schema(description = "Contact person name", example = "John Doe")
    private String name;

    @Schema(description = "Indicates if the provider is active", example = "true")
    private boolean active;
}
