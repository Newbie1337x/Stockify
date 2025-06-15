package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.model.enums.Status;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.specification.EmployeeSpecifications;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /**
     * Crea un nuevo empleado en el sistema.
     * 
     * @param employeeRequest DTO con los datos del empleado a crear
     * @return La entidad del empleado creado
     */
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
       EmployeeEntity employee = employeeMapper.toEntity(employeeRequest);
       employee.setActive(true);
       employee.setStatus(Status.OFFLINE);
      return  employeeMapper
              .toResponseDto
                      (employeeRepository
                              .save(employee));
    }

    /**
     * Busca empleados aplicando filtros y paginación.
     *
     * @param name Nombre del empleado (opcional)
     * @param lastName Apellido del empleado (opcional)
     * @param dni DNI del empleado (opcional)
     * @param pageable Información de paginación
     * @return Página de empleados que cumplen con los filtros
     */
    public Page<EmployeeResponse> getEmployees( String name, String lastName, String dni, Pageable pageable) {
        Specification<EmployeeEntity> spec = Specification
                .where(EmployeeSpecifications.isActive())
                .and(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasLastName(lastName))
                .and(EmployeeSpecifications.hasDni(dni));

        Page<EmployeeEntity> entities = employeeRepository.findAll(spec, pageable);
        return entities.map(employeeMapper::toResponseDto);
    }

    /**
     * Busca un empleado por su ID.
     * 
     * @param id ID del empleado a buscar
     * @return DTO con los datos del empleado encontrado
     * @throws NotFoundException si no se encuentra ningún empleado con el ID especificado
     */
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
    }

    /**
     * Elimina lógicamente un empleado (lo marca como inactivo).
     * 
     * @param id ID del empleado a eliminar
     * @throws NotFoundException si no se encuentra el empleado con el ID especificado
     */
    public void delete(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("No se encontro el empleado"));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    /**
     * Actualiza los datos de un empleado existente.
     * 
     * @param employeeEntity DTO con los nuevos datos del empleado
     * @param id ID del empleado a actualizar
     * @return La entidad del empleado actualizada
     */
    public EmployeeEntity updateEmployee(EmployeeRequest employeeEntity, Long id) {
        EmployeeEntity employee = employeeMapper.toEntity(employeeEntity);
        employee.setId(id);
        employeeRepository.save(employee);
        return employee;
    }

    /**
     * Busca un empleado por su DNI.
     * 
     * @param dni DNI del empleado a buscar
     * @return Optional con la entidad del empleado si existe, empty si no existe
     */
    public Optional<EmployeeEntity> getEmployeeEntityByDni(String dni) {
        return employeeRepository.getEmployeeEntityByDni(dni);
    }
}
