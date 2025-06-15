package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.client.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.entity.ClientEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfRegistration", ignore = true)
    ClientEntity toEntity(ClientRequest clientRequest);
    @Mapping(target = "links", ignore = true)
    ClientResponse toDto(ClientEntity clientEntity);

    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfRegistration", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateClientEntity(ClientRequest clientRequest, @MappingTarget ClientEntity clientEntity);

    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfRegistration", ignore = true)
    void updateClientEntity(ClientRequest clientRequest, @MappingTarget ClientEntity clientEntity);
}