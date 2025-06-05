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
    SessionPosEntity toEntity(SessionPosRequest request);
}
