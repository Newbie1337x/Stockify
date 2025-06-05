package org.stockify.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.stockify.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;

@Mapper(componentModel = "spring")
public interface PosMapper {

    /**
     * Convierte una entidad {@link PosEntity} en un objeto de respuesta {@link PosResponse}.
     *
     * @param posEntity la entidad que se desea mapear.
     * @return el DTO correspondiente de tipo {@link PosResponse}.
     */
    PosResponse toDto(PosEntity posEntity);


    //Que hace bean mapping?
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PosResponse partialUpdate(PosEntity posEntity, @MappingTarget PosResponse posResponse);
}
