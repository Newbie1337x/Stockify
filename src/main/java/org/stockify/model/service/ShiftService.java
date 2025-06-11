package org.stockify.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.shift.ShiftFilterRequest;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.ShiftEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.repository.ShiftRepository;
import org.stockify.model.specification.ShiftSpecification;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public ShiftService(ShiftRepository shiftRepository, ShiftMapper shiftMapper, EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public ShiftResponse save(ShiftRequest shiftRequest) {
        ShiftEntity shiftEntity = shiftMapper.toEntity(shiftRequest);
        List<EmployeeEntity> employees = employeeRepository.findAllById(shiftRequest.getEmployeeIds());
        if(employees.isEmpty()) {
            throw new NotFoundException("Employee not found");
        }

        shiftEntity.setEmployeeEntities(employees);
        ShiftEntity savedEntity = shiftRepository.save(shiftEntity);


        return shiftMapper.toDto(shiftRepository.save(savedEntity));
    }

    public void delete (Long id) {
        if(!shiftRepository.existsById(id)) {
            throw new NotFoundException("Shift with ID " + id + " not found");
        }
        shiftRepository.deleteById(id);
    }

    public ShiftResponse findById(Long id) {
        ShiftEntity shiftEntity = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
        return shiftMapper.toDto(shiftEntity);
    }

    public Page<ShiftResponse> findAll(ShiftFilterRequest filterRequest, Pageable pageable) {
        Specification<ShiftEntity> specification = Specification
                .where(ShiftSpecification.dayBetween(filterRequest.getDayFrom(), filterRequest.getDayTo()))
                .and(ShiftSpecification.entryTimeBetween(filterRequest.getEntryTimeFrom(), filterRequest.getEntryTimeTo()))
                .and(ShiftSpecification.exitTimeBetween(filterRequest.getExitTimeFrom(), filterRequest.getExitTimeTo()))
                .and(ShiftSpecification.hasAnyEmployeeId(filterRequest.getEmployeesIds()));

        Page<ShiftEntity> shiftEntities = shiftRepository.findAll(specification, pageable);
        return shiftEntities.map(shiftMapper::toDto);
    }

    public List<EmployeeResponse> findEmployeesByShiftId(Long shiftId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found")) ;

        return shiftEntity.getEmployeeEntities()
                .stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ShiftResponse updateShiftPartial(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.partialUpdateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    public ShiftResponse updateShiftFull(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.updateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }
}
