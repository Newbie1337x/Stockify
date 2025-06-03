package org.stockify.dto.response;

public record StoreResponse(
    Long id,
    String storeName,
    String address,
    String city
) {}