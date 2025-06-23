package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.shift.ShiftCreateRequest;
import org.stockify.dto.request.shift.ShiftFilterRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.ShiftEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.repository.ShiftRepository;
import org.stockify.model.specification.ShiftSpecification;


@RequiredArgsConstructor
@Service
public class ShiftsService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public ShiftResponse save(ShiftCreateRequest shiftCreateRequest) {
      EmployeeEntity employee = employeeRepository
                .getEmployeeEntityByDni(shiftCreateRequest.getEmployeeDni())
                .orElseThrow(
                        () -> new NotFoundException("Employee with DNI " + shiftCreateRequest.getEmployeeDni() + " not found")
                );
      ShiftEntity shiftEntity = shiftMapper.toEntity(shiftCreateRequest);
      shiftEntity.setEmployee(employee);
      return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }
   public Page<ShiftResponse> findAll(ShiftFilterRequest filterRequest, Pageable pageable)
   {
       Specification<ShiftEntity> specification = Specification
               .where(ShiftSpecification.dayBetween(filterRequest.getDayFrom(), filterRequest.getDayTo()))
               .and(ShiftSpecification.entryTimeBetween(filterRequest.getEntryTimeFrom(), filterRequest.getEntryTimeTo()))
               .and(ShiftSpecification.exitTimeBetween(filterRequest.getExitTimeFrom(), filterRequest.getExitTimeTo()))
               .and(ShiftSpecification.employeeDni(filterRequest.getEmployeeDni()));
       Page<ShiftEntity> shiftEntities = shiftRepository.findAll(specification, pageable);
       return shiftEntities.map(shiftMapper::toDto);
   }











}
