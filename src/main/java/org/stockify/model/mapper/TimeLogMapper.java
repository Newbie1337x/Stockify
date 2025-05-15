package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeLogMapper {
    TimeLogEntity toEntity(TimeLogRequest dto);
    TimeLogResponse toResponse(TimeLogEntity dto);
    List<TimeLogResponse> toResponseList(List<TimeLogEntity> entities);
}
