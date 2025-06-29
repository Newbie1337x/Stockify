package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.product.ProductCSVRequest;
import org.stockify.dto.request.product.ProductRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", uses = {StockMapper.class})
public interface ProductMapper {

    @Mapping(source = "categories", target = "categories", qualifiedByName = "entitiesToNames")
    @Mapping(source = "providers", target = "providers", qualifiedByName = "providerEntitiesToIds")
    @Mapping(source = "stocks", target = "stocks")
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "providers", ignore = true)
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
    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    /**
     * PATCH: ignora campos nulos, actualiza los que vienen con datos
     */
    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "stocks", ignore = true)
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "detailTransactions", ignore = true)
    ProductEntity toEntity(ProductCSVRequest dto);
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToCategorySet")
    ProductRequest toRequest(ProductCSVRequest dto);

    @Named("stringToCategorySet")
    static Set<String> mapCategories(String value) {
        if (value == null || value.isBlank()) return Set.of();
        return Stream.of(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
