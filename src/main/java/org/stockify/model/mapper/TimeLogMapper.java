package org.stockify.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.entity.TimeLogEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface TimeLogMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target ="employee",expression = "java(mapEmployee(dto.getEmployeeId()))")
    @Mapping(target ="store",expression = "java(mapStore(dto.getStoreId()))")
    TimeLogEntity toEntity(TimeLogRequest dto);
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeFullName", expression = "java(getEmployeeFullName(dto.getEmployee()))")
    @Mapping(target = "storeId", source = "store.id")
    TimeLogResponse toResponse(TimeLogEntity dto);
    List<TimeLogResponse> toResponseList(List<TimeLogEntity> entities);

    default EmployeeEntity mapEmployee(Long employeeId) {
        if (employeeId == null) return null;
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);
        return employee;
    }

    default StoreEntity mapStore(Long storeId) {
        if (storeId == null) return null;
        StoreEntity store = new StoreEntity();
        store.setId(storeId);
        return store;
    }

    default String getEmployeeFullName(EmployeeEntity employee) {
        if (employee == null) return null;
        return employee.getName() + " " + employee.getLastName();
    }
}
