package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.util.StringUtils;

import java.util.List;

@Mapper(componentModel = "spring",uses = StringUtils.class)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest dto);
    CategoryResponse toResponse(CategoryEntity entity);
    List<CategoryResponse> toResponseList(List<CategoryEntity> entities);

}
