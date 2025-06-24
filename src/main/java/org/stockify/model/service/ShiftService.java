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
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.repository.ShiftRepository;
import org.stockify.model.specification.ShiftSpecification;
import org.stockify.security.service.AuthService;


@RequiredArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeRepository employeeRepository;
    private final AuthService authService;

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

   public Page<ShiftResponse> findAll(ShiftFilterRequest filterRequest, Pageable pageable) {
       Specification<ShiftEntity> specification = Specification
               .where(ShiftSpecification.dayBetween(filterRequest.getDayFrom(), filterRequest.getDayTo()))
               .and(ShiftSpecification.entryTimeBetween(filterRequest.getEntryTimeFrom(), filterRequest.getEntryTimeTo()))
               .and(ShiftSpecification.exitTimeBetween(filterRequest.getExitTimeFrom(), filterRequest.getExitTimeTo()))
               .and(ShiftSpecification.hasEmployeeDni(filterRequest.getEmployeeDni()));
       Page<ShiftEntity> shiftEntities = shiftRepository.findAll(specification, pageable);
       return shiftEntities.map(shiftMapper::toDto);
   }

   public void delete(Long id) {
        ShiftEntity shiftEntity = shiftRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
      shiftRepository.delete(shiftEntity);
   }

    /**
     * Retrieves the authenticated employee's shifts with pagination support.
     *
     * @param pageable Pagination configuration (page number, size, sort).
     * @return Page of ShiftEntity objects belonging to the authenticated employee.
     */
    public Page<ShiftResponse> getMyShifts(Pageable pageable) {
        EmployeeEntity authEmployee = authService.getAuthenticatedEmployee();
        String dni = authEmployee.getDni();
        Specification<ShiftEntity> spec = Specification.where(ShiftSpecification.hasEmployeeDni(dni));
        Page<ShiftEntity> shifts = shiftRepository.findAll(spec, pageable);
        return shifts.map(shiftMapper::toDto);
    }

    public ShiftResponse findById(Long id) {
        return shiftRepository.findById(id)
                .map(shiftMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
    }
}
