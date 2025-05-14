package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.EmployeeNotFoundException;
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;

import java.util.List;

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

    public List<EmployeeResponse> getAllEmplyeesActive(){
        return employeeMapper.toResponseDtoList(employeeRepository.findAll()
                .stream()
                .filter(EmployeeEntity::getActive)
                .toList());
    }

    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
    }

    public List<EmployeeResponse> getEmployeeByName(String name){
        return  employeeRepository.getEmployeeEntitiesByName(name)
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    public List<EmployeeResponse> getEmployeeByLastName(String lastName){
        return employeeRepository.getEmployeeEntitiesByLastName(lastName)
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    public void delete(Long id) {
       EmployeeEntity employeeEntity = employeeMapper.toEntity(getEmployeeById(id));
       employeeEntity.setActive(false);
       employeeRepository.save(employeeEntity);
    }

    public void updateEmployee(EmployeeRequest employeeEntity) {
        employeeRepository.save(employeeMapper.toEntity(employeeEntity));
    }

    public List<EmployeeResponse> findByStatus(Status statusRequest) {
        return employeeRepository.findByStatus(statusRequest)
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }


    public Status toggleStatus(Long id) {
        // Recupera o lanza excepción si no existe
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
