package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.TransactionEntity;

@Mapper(componentModel = "spring", uses = {ProviderMapper.class})
public interface TransactionMapper {
    TransactionEntity toEntity(TransactionRequest transactionRequest);

    TransactionResponse toDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionRequest transactionRequest, @MappingTarget TransactionEntity transactionEntity);
}