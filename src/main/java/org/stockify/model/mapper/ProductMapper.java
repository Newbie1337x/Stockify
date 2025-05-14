package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categories", target = "categories", qualifiedByName = "entitiesToNames")
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    ProductEntity toEntity(ProductRequest request);

    /**
     * PUT: actualiza todo (incluso campos nulos)
     */
    default void updateEntityFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity) {
        updateFromRequest(dto, entity);
        entity.setCategories(namesToEntities(dto.categories()));
    }

    /**
     * Lógica interna del update (excepto categorías)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    /**
     * PATCH: ignora campos nulos, actualiza los que vienen con datos
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categories", target = "categories", qualifiedByName = "namesToEntities")
    void patchEntityFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    @Named("entitiesToNames")
    default Set<String> entitiesToNames(Set<CategoryEntity> categories) {
        if (categories == null || categories.isEmpty()) return Set.of();
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());
    }

    @Named("namesToEntities")
    default Set<CategoryEntity> namesToEntities(Set<String> names) {
        if (names == null || names.isEmpty()) return Set.of();
        return names.stream()
                .map(name -> {
                    CategoryEntity e = new CategoryEntity();
                    e.setName(name);
                    return e;
                })
                .collect(Collectors.toSet());
    }
}