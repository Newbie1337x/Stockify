package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.entity.ProviderEntity;


@Mapper (componentModel = "spring")
public interface ProviderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    ProviderEntity toEntity(ProviderRequest dto);
    ProviderResponse toResponseDTO(ProviderEntity entity);

}

