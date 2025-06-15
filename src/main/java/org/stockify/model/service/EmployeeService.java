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
     * Obtiene todos los empleados del sistema.
     * 
     * @return Lista de todos los empleados convertidos a DTO de respuesta
     */
    public List<EmployeeResponse> getAllEmployees() {
        return employeeMapper.toResponseDtoList(employeeRepository.findAll());
    }

    /**
     * Obtiene todos los empleados activos (con estado ONLINE).
     * 
     * @return Lista de empleados activos convertidos a DTO de respuesta
     */
    public List<EmployeeResponse> getAllEmplyeesActive() {
        return findByStatus(Status.ONLINE);
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
     * Verifica si existe un empleado con el DNI especificado.
     * 
     * @param dni DNI a verificar
     * @return true si existe un empleado con ese DNI, false en caso contrario
     */
    public Boolean existByDni(String dni) {
        return employeeRepository.existsByDni(dni);
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

    /*
    public EmployeeEntity getEmployeeEntityById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
    }

    public EmployeeResponse getEmployeeByDni(String dni) {
        return employeeRepository.findByDni(dni);
    }

    public List<EmployeeResponse> getEmployeeByName(String name) {
        return employeeRepository.getEmployeeEntitiesByName(name)
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    public List<EmployeeResponse> getEmployeeByLastName(String lastName) {
        return employeeRepository.getEmployeeEntitiesByLastName(lastName)
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }*/

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
     * Busca empleados por su estado y que estén activos.
     * 
     * @param statusRequest Estado a buscar (ONLINE/OFFLINE)
     * @return Lista de empleados que coinciden con el estado y están activos
     */
    public List<EmployeeResponse> findByStatus(Status statusRequest) {
        return employeeRepository.findByStatus(statusRequest)
                .stream()
                .filter(EmployeeEntity::getActive)
                .map(employeeMapper::toResponseDto)
                .toList();
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

    /**
     * Cambia el estado de un empleado entre ONLINE y OFFLINE.
     * 
     * @param id ID del empleado cuyo estado se cambiará
     * @return El nuevo estado del empleado
     * @throws NotFoundException si no se encuentra el empleado con el ID especificado
     */
    public Status toggleStatus(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found: " + id));
        Status next = (employee.getStatus() == Status.ONLINE)
                ? Status.OFFLINE
                : Status.ONLINE;
        employee.setStatus(next);
        employeeRepository.save(employee);
        return next;
    }
}
