package org.stockify.dto.request.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StoreRequest(
    @NotBlank(message = "Store name is required")
    @Size(max = 100, message = "Store name must be less than 100 characters")
    String storeName,
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be less than 255 characters")
    String address,
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    String city
) {}