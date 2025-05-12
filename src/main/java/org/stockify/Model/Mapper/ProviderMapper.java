package org.stockify.Model.Mapper;

import org.mapstruct.Mapper;
import org.stockify.Model.DTO.Request.ProviderRequestDTO;
import org.stockify.Model.DTO.Response.ProviderResponseDTO;
import org.stockify.Model.Entities.ProviderEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    ProviderEntity toEntity(ProviderRequestDTO dto);
    ProviderResponseDTO toResponseDTO(ProviderEntity entity);
    List<ProviderResponseDTO> toResponseDTOList(List<ProviderEntity> entities);
}
