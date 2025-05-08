package org.stockify.model.dto.response;

public record ProductResponse(
        int id,
        String name,
        double price) {
}
