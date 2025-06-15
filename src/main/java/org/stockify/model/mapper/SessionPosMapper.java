package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = PosMapper.class)
public interface SessionPosMapper {
    @Mapping(source = "posEntity.id", target = "idPos")
    SessionPosResponse toDto(SessionPosEntity entity);
    @Mapping(source = "posEntity.id", target = "idPos")
    SessionPosCreateResponse toDtoCreate(SessionPosEntity entity);
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "posEntity", ignore = true)
    @Mapping(target = "openingTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expectedAmount", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "closeTime", ignore = true)
    @Mapping(target = "closeAmount", ignore = true)
    @Mapping(target = "cashDifference", ignore = true)
    SessionPosEntity toEntity(SessionPosRequest request);
}
