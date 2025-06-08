package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.entity.PurchaseEntity;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(source = "transactionId", target = "transaction.id")
    @Mapping(source = "providerId", target = "provider.id")
    PurchaseEntity toEntity(PurchaseRequest dto);

    PurchaseResponse toResponseDTO(PurchaseEntity entity);
}
