package org.stockify.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.stockify.dto.request.pos.PosOpenRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;

@Mapper(componentModel = "spring")
public interface PosMapper {
    /**
     * Convierte un DTO {@link PosOpenRequest} en una entidad {@link PosEntity}.
     *
     * @param PosOpenRequest el DTO que se desea mapear.
     * @return la entidad resultante de tipo {@link PosEntity}.
     */
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sessionPosEntities", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    PosEntity toEntity(PosOpenRequest PosOpenRequest);


    /**
     * Convierte una entidad {@link PosEntity} en un objeto de respuesta {@link PosResponse}.
     *
     * @param posEntity la entidad que se desea mapear.
     * @return el DTO correspondiente de tipo {@link PosResponse}.
     */
    @Mapping(target = "idStore", source = "store.id")
    PosResponse toDto(PosEntity posEntity);


    @Mapping(target = "idStore", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PosResponse partialUpdate(PosEntity posEntity, @MappingTarget PosResponse posResponse);
}
