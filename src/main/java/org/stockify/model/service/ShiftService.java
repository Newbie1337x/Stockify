package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
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

/**
 * Service class responsible for managing shift-related operations.
 * It provides methods to create, retrieve, update, and delete shifts,
 * as well as to assign or remove employees from shifts.
 */
@RequiredArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /**
     * Creates and persists a new shift based on the given request.
     *
     * @param shiftRequest DTO containing the details of the shift to be created
     * @return DTO representing the created shift
     * @throws NotFoundException if no employees with the specified IDs are found
     */
    public ShiftResponse save(ShiftRequest shiftRequest) {
        ShiftEntity shiftEntity = shiftMapper.toEntity(shiftRequest);
        List<EmployeeEntity> employees = employeeRepository.findAllById(shiftRequest.getEmployeeIds());
        if (employees.isEmpty()) {
            throw new NotFoundException("Employee not found");
        }
        shiftEntity.setEmployeeEntities(employees);
        ShiftEntity savedEntity = shiftRepository.save(shiftEntity);
        return shiftMapper.toDto(savedEntity);
    }

    /**
     * Deletes a shift by its ID.
     *
     * @param id the ID of the shift to be deleted
     * @throws NotFoundException if no shift exists with the specified ID
     */
    public void delete(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new NotFoundException("Shift with ID " + id + " not found");
        }
        shiftRepository.deleteById(id);
    }

    /**
     * Retrieves a shift by its ID.
     *
     * @param id the ID of the shift to be retrieved
     * @return DTO containing the shift's information
     * @throws NotFoundException if no shift exists with the specified ID
     */
    public ShiftResponse findById(Long id) {
        ShiftEntity shiftEntity = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
        return shiftMapper.toDto(shiftEntity);
    }

    /**
     * Retrieves all shifts that match the given filter criteria, with pagination support.
     *
     * @param filterRequest DTO containing the filter parameters such as day range, entry and exit times, and employee IDs
     * @param pageable pagination parameters
     * @return a page of shifts that match the given filters
     */
    public Page<ShiftResponse> findAll(ShiftFilterRequest filterRequest, Pageable pageable) {
        Specification<ShiftEntity> specification = Specification
                .where(ShiftSpecification.dayBetween(filterRequest.getDayFrom(), filterRequest.getDayTo()))
                .and(ShiftSpecification.entryTimeBetween(filterRequest.getEntryTimeFrom(), filterRequest.getEntryTimeTo()))
                .and(ShiftSpecification.exitTimeBetween(filterRequest.getExitTimeFrom(), filterRequest.getExitTimeTo()))
                .and(ShiftSpecification.hasAnyEmployeeId(filterRequest.getEmployeesIds()));

        Page<ShiftEntity> shiftEntities = shiftRepository.findAll(specification, pageable);
        return shiftEntities.map(shiftMapper::toDto);
    }

    /**
     * Retrieves all employees assigned to a specific shift.
     *
     * @param shiftId the ID of the shift
     * @return a list of employee DTOs assigned to the shift
     * @throws NotFoundException if the shift with the given ID is not found
     */
    public List<EmployeeResponse> findEmployeesByShiftId(Long shiftId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));

        return shiftEntity.getEmployeeEntities()
                .stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Partially updates a shift with the fields provided in the request.
     *
     * @param id the ID of the shift to be updated
     * @param shiftRequest DTO containing the fields to update
     * @return DTO representing the updated shift
     * @throws NotFoundException if no shift exists with the specified ID
     */
    public ShiftResponse updateShiftPartial(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.partialUpdateShiftEntity(shiftRequest, existingShift);
        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    /**
     * Fully replaces a shift with the data provided in the request.
     *
     * @param id the ID of the shift to be replaced
     * @param shiftRequest DTO containing the new shift data
     * @return DTO representing the updated shift
     * @throws NotFoundException if no shift exists with the specified ID
     */
    public ShiftResponse updateShiftFull(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.updateShiftEntity(shiftRequest, existingShift);
        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    /**
     * Removes a specific employee from a given shift.
     *
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to remove
     * @return DTO representing the updated shift
     * @throws NotFoundException if the shift or employee is not found
     */
    public ShiftResponse deleteEmployeeFromShift(Long shiftId, Long employeeId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee with ID " + employeeId + " not found"));

        shiftEntity.getEmployeeEntities().remove(employeeEntity);
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }

    /**
     * Removes all employees from a given shift.
     *
     * @param shiftId the ID of the shift
     * @return DTO representing the updated shift
     * @throws NotFoundException if the shift is not found
     */
    public ShiftResponse deleteAllEmployeesFromShift(Long shiftId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        shiftEntity.getEmployeeEntities().clear();
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }

    /**
     * Adds a specific employee to a given shift.
     *
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to add
     * @return DTO representing the updated shift
     * @throws NotFoundException if the shift or employee is not found
     */
    public ShiftResponse addEmployeeToShift(Long shiftId, Long employeeId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee with ID " + employeeId + " not found"));

        shiftEntity.getEmployeeEntities().add(employeeEntity);
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }
}
