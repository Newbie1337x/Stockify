package org.stockify.dto.request.provider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProviderRequest {

    @Schema(description = "Contact person's name", example = "John Supplier", minLength = 3, maxLength = 100)
    @NotBlank(message = "Contact name is required")
    @Size(min = 3, max = 100)
    private String name;

    @Schema(description = "Phone number", example = "+1-555-1234567")
    private String phone;

    @Schema(description = "Email address", example = "contact@example.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Tax Identification Number (e.g., VAT number)", example = "12345678")
    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @Schema(description = "Tax address", example = "123 Supplier St, City")
    private String taxAddress;

    @Schema(description = "Registered business name", example = "Supplier Inc.")
    @NotBlank(message = "Business name is required")
    private String businessName;
}
