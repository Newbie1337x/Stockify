package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.shift.ShiftCreateRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.ShiftEntity;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

    ShiftEntity toEntity(ShiftCreateRequest shiftCreateRequest);
    ShiftResponse toDto(ShiftEntity shiftEntity);
}

