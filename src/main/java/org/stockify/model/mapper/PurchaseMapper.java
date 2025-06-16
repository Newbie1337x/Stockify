package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.SaleEntity;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface PurchaseMapper {

    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "providerId", target = "provider.id")
    PurchaseEntity toEntity(PurchaseRequest dto);

    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "toTransactionResponse")
    PurchaseResponse toResponseDTO(PurchaseEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdatePurchaseEntity(SaleRequest saleRequest, @MappingTarget SaleEntity saleEntity);

}
