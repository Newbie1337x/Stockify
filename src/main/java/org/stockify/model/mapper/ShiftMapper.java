package org.stockify.model.mapper;

import org.mapstruct.*;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.ShiftEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeEntities", ignore = true)
    ShiftEntity toEntity(ShiftRequest shiftRequest);
    @Mapping(target = "employeeIds", ignore = true)
    ShiftResponse toDto(ShiftEntity shiftEntity);
    List<ShiftResponse> toDtoList(List<ShiftEntity> shiftEntities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeEntities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateShiftEntity(ShiftRequest shiftRequest, @MappingTarget ShiftEntity shiftEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeEntities", ignore = true)
    void updateShiftEntity(ShiftRequest shiftRequest, @MappingTarget ShiftEntity shiftEntity);
}
