package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.ShiftEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    ShiftEntity toEntity(ShiftRequest shiftRequest);
    ShiftResponse toDto(ShiftEntity shiftEntity);
    List<ShiftResponse> toDtoList(List<ShiftEntity> shiftEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateShiftEntity(ShiftRequest shiftRequest, @MappingTarget ShiftEntity shiftEntity);

    void updateShiftEntity(ShiftRequest shiftRequest, @MappingTarget ShiftEntity shiftEntity);
}