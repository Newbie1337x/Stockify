package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.stock.StockRequest;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;
import org.stockify.model.entity.StoreEntity;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "lowStockAlertSent", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "quantity", source = "request.quantity")
    StockEntity toEntity(StockRequest request, ProductEntity product, StoreEntity store);

    @Mapping(target = "product_id", source = "product.id")
    @Mapping(target = "store_id", source = "store.id")
    @Mapping(target = "stock", source = "quantity")
    StockResponse toResponse(StockEntity entity);
}
