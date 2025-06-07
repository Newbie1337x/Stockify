package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.security.model.dto.request.CredentialRequest;
import org.stockify.security.model.entity.CredentialsEntity;

@Mapper(componentModel = "spring")
public interface CredentialMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true) // lo inyecto en el service
    @Mapping(target = "employee", ignore = true) // este tambien lo inyecto en el service
    CredentialsEntity toEntity(CredentialRequest dto);
}
