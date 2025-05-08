package org.stockify.model.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.util.StringUtils;
import java.util.List;

@Mapper(componentModel = "spring"
)
public interface ProductMapper {

    // De Entity a Response
    @Mapping(source = "name", target = "name", qualifiedByName = "capitalizeFirst")
    ProductResponse toResponse(ProductEntity entity);

    List<ProductResponse> toResponseList(List<ProductEntity> entities);

    @Named("capitalizeFirst")
    default String capitalizeFirst(String name) {
        return StringUtils.capitalizeFirst(name);
    }

    // De Request a Entity
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductRequest request);
}


