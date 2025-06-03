package org.stockify.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.stockify.dto.request.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.entities.ClientEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity toEntity(ClientRequest clientRequest);
    ClientResponse toDto(ClientEntity clientEntity);
    List<ClientResponse> toDtoList(List<ClientEntity> clientEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateClientEntity(ClientRequest clientRequest, @MappingTarget ClientEntity clientEntity);

    void updateClientEntity(ClientRequest clientRequest, @MappingTarget ClientEntity clientEntity);
}