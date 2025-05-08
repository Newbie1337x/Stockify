package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.util.StringUtils;

import java.util.List;

@Mapper(componentModel = "spring",uses = StringUtils.class)
public interface CategoryMapper {

    @Mapping(source = "name", target = "name",qualifiedByName = "capitalizeFirst")
    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest dto);
    CategoryResponse toResponse(CategoryEntity entity);
    List<CategoryResponse> toResponseList(List<CategoryEntity> entities);

    @Named("capitalizeFirst")
    default String capitalizeFirst(String name) {
        return StringUtils.capitalizeFirst(name);
    }
}
