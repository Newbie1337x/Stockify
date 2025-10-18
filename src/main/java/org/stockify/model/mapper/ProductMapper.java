package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.product.ProductCSVRequest;
import org.stockify.dto.request.product.ProductRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categories", target = "categories", qualifiedByName = "entitiesToNames")
    @Mapping(source = "providers", target = "providers", qualifiedByName = "providerEntitiesToIds")
    @Mapping(target = "stock", expression = "java(mapStock(entity.getStock()))")
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "stock", expression = "java(normalizeStock(request.stock()))")
    ProductEntity toEntity(ProductRequest request);

    /**
     * PUT: actualiza todo (incluso campos nulos)
     */
    default void updateEntityFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity) {
        updateFromRequest(dto, entity);
        entity.setCategories(namesToEntities(dto.categories()));
        if (dto.stock() != null) {
            entity.setStock(normalizeStock(dto.stock()));
        }
    }

    /**
     * Lógica interna del update (excepto categorías)
     */
    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    /**
     * PATCH: ignora campos nulos, actualiza los que vienen con datos
     */
    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categories", target = "categories", qualifiedByName = "namesToEntities")
    void patchEntityFromRequest(ProductRequest dto, @MappingTarget ProductEntity entity);

    // ✅ Todos estos devuelven colecciones MUTABLES (ya no Set.of())
    @Named("entitiesToNames")
    default Set<String> entitiesToNames(Set<CategoryEntity> categories) {
        if (categories == null || categories.isEmpty()) return new LinkedHashSet<>();
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Named("namesToEntities")
    default Set<CategoryEntity> namesToEntities(Set<String> names) {
        if (names == null || names.isEmpty()) return new LinkedHashSet<>();
        return names.stream()
                .map(name -> {
                    CategoryEntity e = new CategoryEntity();
                    e.setName(name);
                    return e;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Named("providerEntitiesToIds")
    default Set<Long> providerEntitiesToIds(Set<ProviderEntity> providers) {
        if (providers == null || providers.isEmpty()) return new LinkedHashSet<>();
        return providers.stream()
                .map(ProviderEntity::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "detailTransactions", ignore = true)
    @Mapping(target = "stock", expression = "java(normalizeStock(dto.getStock()))")
    ProductEntity toEntity(ProductCSVRequest dto);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToCategorySet")
    ProductRequest toRequest(ProductCSVRequest dto);

    @Named("stringToCategorySet")
    static Set<String> mapCategories(String value) {
        if (value == null || value.isBlank()) return new LinkedHashSet<>();
        return Stream.of(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    default double mapStock(BigDecimal stock) {
        return stock == null ? 0d : stock.doubleValue();
    }

    default BigDecimal normalizeStock(BigDecimal stock) {
        return stock == null ? BigDecimal.ZERO : stock;
    }
}
