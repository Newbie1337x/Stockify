package org.stockify.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.stockify.dto.request.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.entities.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity toEnity(ClientRequest clientRequest);
    ClientResponse toDto(ClientEntity clientEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientResponse partialUpdate(ClientEntity clientEntity, @MappingTarget ClientResponse clientResponse);
}


