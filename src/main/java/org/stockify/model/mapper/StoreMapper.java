package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.StoreRequest;
import org.stockify.dto.response.StoreResponse;
import org.stockify.model.entity.StoreEntity;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    @Mapping(target = "id", ignore = true)
    StoreEntity toEntity(StoreRequest request);

    StoreResponse toResponse(StoreEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreRequest request, @MappingTarget StoreEntity entity);
}