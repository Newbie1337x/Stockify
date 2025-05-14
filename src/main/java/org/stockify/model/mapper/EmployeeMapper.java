package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeEntity toEntity(EmployeeRequest dto);
    //EmployeeEntity toResposeToEntity(EmployeeResponse dto);
    EmployeeResponse toResponseDto(EmployeeEntity dto);
    List<EmployeeResponse> toResponseDtoList(List<EmployeeEntity> entities);
}
