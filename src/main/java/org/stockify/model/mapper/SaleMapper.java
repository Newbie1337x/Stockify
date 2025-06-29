package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.model.entity.SaleEntity;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface SaleMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "id", ignore = true)
    SaleEntity toEntity(SaleRequest dto);

    @Mapping(target = "clientDni", source = "client.dni")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "toTransactionResponse")
    SaleResponse toResponseDTO (SaleEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateSaleEntity(SaleRequest saleRequest, @MappingTarget SaleEntity saleEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    void updateShiftEntity(SaleRequest saleRequest, @MappingTarget SaleEntity saleEntity);
}
