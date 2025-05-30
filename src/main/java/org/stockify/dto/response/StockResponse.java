package org.stockify.dto.response;

public record StockResponse(Long product_id, Long store_id, Double stock) {
}
