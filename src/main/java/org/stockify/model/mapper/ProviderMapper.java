package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.stockify.dto.request.ProviderRequestDTO;
import org.stockify.dto.response.ProviderResponseDTO;
import org.stockify.model.entities.ProviderEntity;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ProviderMapper {

    ProviderEntity toEntity(ProviderRequestDTO dto);

    ProviderResponseDTO toResponseDTO(ProviderEntity entity);

    List<ProviderResponseDTO> toResponseDTOList(List<ProviderEntity> entities);
}
