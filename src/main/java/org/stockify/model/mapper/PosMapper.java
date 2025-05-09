package org.stockify.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.stockify.model.dto.request.PosRequest;
import org.stockify.model.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;

@Mapper(componentModel = "spring")
public interface PosMapper {
    /**
     * Convierte un DTO {@link PosRequest} en una entidad {@link PosEntity}.
     *
     * @param posRequest el DTO que se desea mapear.
     * @return la entidad resultante de tipo {@link PosEntity}.
     */
    PosEntity toEntity(PosRequest posRequest);

    /**
     * Convierte una entidad {@link PosEntity} en un objeto de respuesta {@link PosResponse}.
     *
     * @param posEntity la entidad que se desea mapear.
     * @return el DTO correspondiente de tipo {@link PosResponse}.
     */
    PosResponse toDto(PosEntity posEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PosResponse partialUpdate(PosEntity posEntity, @MappingTarget PosResponse posResponse);
}
