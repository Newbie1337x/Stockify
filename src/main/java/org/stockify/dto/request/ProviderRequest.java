package org.stockify.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProviderRequest {

    @NotBlank(message = "Contact name is required")
    @Size(min = 3, max = 100)
    private String name;

    private String phone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    private String taxAddress;

    @NotBlank(message = "Business name is required")
    private String businessName;
}