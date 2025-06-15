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
@RequiredArgsConstructor

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /**
     * Guarda un nuevo turno en el sistema.
     * 
     * @param shiftRequest DTO con los datos del turno a crear
     * @return DTO con los datos del turno creado
     * @throws NotFoundException si no se encuentran los empleados especificados
     */
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

    /**
     * Elimina un turno por su ID.
     * 
     * @param id ID del turno a eliminar
     * @throws NotFoundException si no se encuentra ningún turno con el ID especificado
     */
    public void delete (Long id) {
        if(!shiftRepository.existsById(id)) {
            throw new NotFoundException("Shift with ID " + id + " not found");
        }
        shiftRepository.deleteById(id);
    }

    /**
     * Busca un turno por su ID.
     * 
     * @param id ID del turno a buscar
     * @return DTO con los datos del turno encontrado
     * @throws NotFoundException si no se encuentra ningún turno con el ID especificado
     */
    public ShiftResponse findById(Long id) {
        ShiftEntity shiftEntity = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
        return shiftMapper.toDto(shiftEntity);
    }

    /**
     * Busca turnos aplicando filtros y paginación.
     * 
     * @param filterRequest DTO con los filtros a aplicar (día, hora de entrada, hora de salida, IDs de empleados)
     * @param pageable Información de paginación
     * @return Página de turnos que cumplen con los filtros
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
     * Busca los empleados asignados a un turno.
     * 
     * @param shiftId ID del turno del que se buscarán los empleados
     * @return Lista de DTOs con los datos de los empleados asignados al turno
     * @throws NotFoundException si no se encuentra ningún turno con el ID especificado
     */
    public List<EmployeeResponse> findEmployeesByShiftId(Long shiftId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found")) ;

        return shiftEntity.getEmployeeEntities()
                .stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza parcialmente un turno existente.
     * 
     * @param id ID del turno a actualizar parcialmente
     * @param shiftRequest DTO con los datos a actualizar del turno
     * @return DTO con los datos del turno actualizado
     * @throws NotFoundException si no se encuentra ningún turno con el ID especificado
     */
    public ShiftResponse updateShiftPartial(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.partialUpdateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    /**
     * Actualiza completamente un turno existente.
     * 
     * @param id ID del turno a actualizar
     * @param shiftRequest DTO con los nuevos datos del turno
     * @return DTO con los datos del turno actualizado
     * @throws NotFoundException si no se encuentra ningún turno con el ID especificado
     */
    public ShiftResponse updateShiftFull(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.updateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    public ShiftResponse deleteEmployeeFromShift(Long shiftId, Long employeeId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee with ID " + employeeId + " not found"));
        shiftEntity.getEmployeeEntities().remove(employeeEntity);
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }

    public ShiftResponse deleteAllEmployeesFromShift(Long shiftId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        shiftEntity.getEmployeeEntities().clear();
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }

    public ShiftResponse addEmployeeToShift(Long shiftId, Long employeeId) {
        ShiftEntity shiftEntity = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found"));
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee with ID " + employeeId + " not found"));
        shiftEntity.getEmployeeEntities().add(employeeEntity);
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }
}
