package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.model.entity.TransactionEntity;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionEntity toEntity(TransactionRequest transactionRequest);

    TransactionRequest toDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionRequest transactionRequest, @MappingTarget TransactionEntity transactionEntity);
}