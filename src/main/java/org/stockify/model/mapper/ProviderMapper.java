package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.provider.ProviderCsvRequest;
import org.stockify.dto.request.provider.ProviderRequest;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.entity.ProviderEntity;


@Mapper (componentModel = "spring")
public interface ProviderMapper {

    @Mapping(target = "purchaseList", ignore = true)
    @Mapping(target = "productList", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    ProviderEntity toEntity(ProviderRequest dto);
    ProviderResponse toResponseDTO(ProviderEntity entity);
    ProviderRequest toRequestDTO(ProviderCsvRequest dto);
}

