package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(source = "categories", target = "categories", qualifiedByName = "categoriesToStrings")
    ProductResponse toResponse(ProductEntity entity);

    List<ProductResponse> toResponseList(List<ProductEntity> entities);


    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductRequest request);


    @Named("categoriesToStrings")
    default Set<String> categoriesToStrings(Set<CategoryEntity> categories) {
        if (categories == null) return Set.of();
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());
    }
}
