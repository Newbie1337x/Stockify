package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeEntity toEntity(EmployeeRequest dto);
    EmployeeEntity toEntity(EmployeeResponse dto);
    EmployeeResponse toResponseDto(EmployeeEntity dto);
    List<EmployeeResponse> toResponseDtoList(List<EmployeeEntity> entities);
}
