package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stockify.model.enums.Status;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.specification.EmployeeSpecifications;
import org.stockify.security.model.entity.CredentialsEntity;

import java.util.List;
import java.util.Optional;

/**
 *
 * Service class responsible for managing Employee entities.
 * <p>
 * Provides operations to create, read, update, logically delete, and filter employees,
 * using specifications for flexible querying and pagination support.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /**
     *
     * Creates a new employee record in the system.
     * <p>
     * Sets the employee as active and assigns the default status as OFFLINE.
     * </p>
     *
     * @param employeeRequest Data Transfer Object containing the information of the employee to be created
     * @return EmployeeResponse DTO representing the created employee
     */
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        EmployeeEntity employee = employeeMapper.toEntity(employeeRequest);
        employee.setActive(true);
        employee.setStatus(Status.OFFLINE);
        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    /**
     *
     * Retrieves a paginated list of employees filtered by optional criteria.
     * <p>
     * Applies filters for name, last name, DNI, and active status using JPA Specifications.
     * </p>
     *
     * @param name     Optional employee first name filter
     * @param lastName Optional employee last name filter
     * @param dni      Optional employee DNI filter
     * @param pageable Pageable object defining pagination parameters
     * @return Page of EmployeeResponse DTOs matching the filter criteria
     */
    public Page<EmployeeResponse> getEmployees(String name, String lastName, String dni, Pageable pageable) {
        Specification<EmployeeEntity> spec = Specification
                .where(EmployeeSpecifications.isActive())
                .and(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasLastName(lastName))
                .and(EmployeeSpecifications.hasDni(dni));

        Page<EmployeeEntity> entities = employeeRepository.findAll(spec, pageable);
        return entities.map(employeeMapper::toResponseDto);
    }

    /**
     * Retrieves the employee profile associated with the current authentication context.
     *
     * @param authentication The Spring Security authentication object containing the user's credentials
     * @return The EmployeeEntity associated with the authenticated user
     */
    public EmployeeEntity getProfile(Authentication authentication){
        CredentialsEntity credentials = (CredentialsEntity) authentication.getPrincipal();
        return credentials.getEmployee();
    }

    /**
     * Retrieves a single employee by its unique identifier.
     *
     * @param id the unique ID of the employee to retrieve
     * @return EmployeeResponse DTO containing the employee data
     * @throws NotFoundException if no employee with the specified ID exists
     */
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
    }

    /**
     *
     * Performs a logical deletion of an employee by marking it as inactive.
     *
     * @param id the unique ID of the employee to delete
     * @throws NotFoundException if no employee with the specified ID exists
     */
    public void delete(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    /**
     *
     * Performs a logical reactivation of an employee by marking it as active.
     *
     * @param id the unique ID of the employee to reactivate
     * @return EmployeeResponse DTO containing the reactivated employee data
     * @throws NotFoundException if no employee with the specified ID exists
     */
    public EmployeeResponse reactivate(Long id) {
        // Use the custom method that bypasses the @Where clause to find inactive employees
        EmployeeEntity employee = employeeRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
        employee.setActive(true);
        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    /**
     *
     * Updates an existing employee's data.
     * <p>
     * Converts the provided EmployeeRequest DTO into an entity and sets its ID before saving.
     * Only allows updating active employees.
     * </p>
     *
     * @param employeeRequest DTO containing the new employee data
     * @param id              the unique ID of the employee to update
     * @return The updated EmployeeEntity after persistence
     * @throws NotFoundException if no active employee with the specified ID exists
     */
    public EmployeeEntity updateEmployee(EmployeeRequest employeeRequest, Long id) {
        EmployeeEntity existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));

        if (!existingEmployee.getActive()) {
            throw new NotFoundException("Cannot update inactive employee with ID: " + id);
        }

        EmployeeEntity employee = employeeMapper.toEntity(employeeRequest);
        employee.setId(id);
        employee.setActive(true); // Ensure active status is preserved
        return employeeRepository.save(employee);
    }

    /**
     *
     * Finds an employee entity by its DNI (national ID number).
     *
     * @param dni the DNI to search for
     * @return an Optional containing the EmployeeEntity if found, or empty if not found
     */
    public Optional<EmployeeEntity> getEmployeeEntityByDni(String dni) {
        return employeeRepository.getEmployeeEntityByDni(dni);
    }

    /**
     * Sets all employees to OFFLINE status.
     * This method is intended to be called on application startup.
     */
    @Transactional
    public void setAllEmployeesToOffline() {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        for (EmployeeEntity employee : employees) {
            employee.setStatus(Status.OFFLINE);
        }
        employeeRepository.saveAll(employees);
    }
}
