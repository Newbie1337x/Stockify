package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.stockify.dto.request.DetailTransactionRequest;
import org.stockify.dto.response.DetailTransactionResponse;
import org.stockify.model.entity.DetailTransactionEntity;

@Mapper(componentModel = "spring")
public interface DetailTransactionMapper {

    DetailTransactionResponse toDto(DetailTransactionEntity entity);
    DetailTransactionEntity toEntity(DetailTransactionRequest request);

}
