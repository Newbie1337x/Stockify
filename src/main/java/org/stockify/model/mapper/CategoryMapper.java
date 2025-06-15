package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.category.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest dto);

    CategoryResponse toResponse(CategoryEntity entity);


    @Mapping(target = "id", ignore = true)
    CategoryEntity updateEntityFromRequest(CategoryRequest dto, @MappingTarget CategoryEntity entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    void patchEntityFromRequest(CategoryRequest dto, @MappingTarget CategoryEntity entity);

}
