package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public EmployeeEntity createEmployee(EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeMapper.toEntity(employeeRequest);
        employeeRepository.save(employeeMapper.toEntity(employeeRequest));
        return employeeEntity;
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeMapper.toResponseDtoList(employeeRepository.findAll());
    }

    public List<EmployeeResponse> getAllEmplyeesActive() {
        return findByStatus(Status.ONLINE);
    }

    public Page<EmployeeResponse> getEmployees(Long id, String name, String lastName, String dni, Pageable pageable) {
        Specification<EmployeeEntity> spec = Specification
                .where(EmployeeSpecifications.isActive())
                .and(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasLastName(lastName))
                .and(EmployeeSpecifications.hasDni(dni));

        Page<EmployeeEntity> entities = employeeRepository.findAll(spec, pageable);
        return entities.map(employeeMapper::toResponseDto);
    }


    public Boolean existByDni(String dni) {
        return employeeRepository.existsByDni(dni);
    }

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

    public void delete(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("No se encontro el empleado"));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    public EmployeeEntity updateEmployee(EmployeeRequest employeeEntity, Long id) {
        EmployeeEntity employee = employeeMapper.toEntity(employeeEntity);
        employee.setId(id);
        employeeRepository.save(employee);
        return employee;
    }

    public List<EmployeeResponse> findByStatus(Status statusRequest) {
        return employeeRepository.findByStatus(statusRequest)
                .stream()
                .filter(EmployeeEntity::getActive)
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    public Optional<EmployeeEntity> getEmployeeEntityByDni(String dni) {
        return employeeRepository.getEmployeeEntityByDni(dni);
    }


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
