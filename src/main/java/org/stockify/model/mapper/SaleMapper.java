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

    @Mapping(target = "dniClient", source = "client.dni")
    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "toTransactionResponse")
    SaleResponse toResponseDTO (SaleEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateSaleEntity(SaleRequest saleRequest, @MappingTarget SaleEntity saleEntity);

    void updateShiftEntity(SaleRequest saleRequest, @MappingTarget SaleEntity saleEntity);
}
