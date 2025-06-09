package org.stockify.model.mapper;

import org.mapstruct.*;
import org.mapstruct.Named;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.TransactionEntity;

@Mapper(componentModel = "spring", uses = {ProviderMapper.class})
public interface TransactionMapper {
    TransactionEntity toEntity(TransactionRequest transactionRequest);

    @Mapping(target = "storeName", source = "store.storeName")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "sessionPosId", source = "sessionPosEntity.id")
    @Mapping(target = "idPos", source = "sessionPosEntity.posEntity.id")
    @Mapping(target = "employeeId", source = "sessionPosEntity.employee.id")
    @Mapping(target = "employeeDni", source = "sessionPosEntity.employee.dni")
    @Named("toTransactionResponse")
    TransactionResponse toDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionRequest transactionRequest, @MappingTarget TransactionEntity transactionEntity);
}
