package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "shiftEntities", ignore = true)
    @Mapping(target = "sessionPosEntities", ignore = true)
    @Mapping(target = "id", ignore = true)
    EmployeeEntity toEntity(EmployeeRequest dto);
    EmployeeResponse toResponseDto(EmployeeEntity dto);
    List<EmployeeResponse> toResponseDtoList(List<EmployeeEntity> entities);
}
