package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.ProductRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductStoreMapper {

    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    ProductEntity toEntity(ProductStoreResponse dto);

    @Mapping(target = "productID", source = "entity.id")
    @Mapping(source = "entity.categories", target = "categories", qualifiedByName = "entitiesToNames")
    @Mapping(source = "entity.providers", target = "providers", qualifiedByName = "providerEntitiesToIds")
    @Mapping(target = "stock", source = "stock")
    ProductStoreResponse toResponse(ProductEntity entity, Double stock);

    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    @Mapping(target = "providers", ignore = true)
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

    @Named("providerEntitiesToIds")
    default Set<Long> providerEntitiesToIds(Set<ProviderEntity> providers) {
        if (providers == null || providers.isEmpty()) return Set.of();
        return providers.stream()
                .map(ProviderEntity::getId)
                .collect(Collectors.toSet());
    }
}