package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.EmployeeNotFoundException;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;

import java.util.ArrayList;
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
        return findByStatus(Status.ONLINE);
    }

<<<<<<< HEAD
    public List<EmployeeResponse> getEmployee(Long id, String name, String lastName){
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        if(id != null){
            EmployeeResponse response = getEmployeeById(id);
            employeeResponses.add(response);
            return employeeResponses;
        }
        else if (name != null){
            employeeResponses = getEmployeeByName(name);
            return employeeResponses;
        }
        else if (lastName != null){
            employeeResponses = getEmployeeByLastName(lastName);
            return employeeResponses;
        }

        return getAllEmplyeesActive();
    }

=======
>>>>>>> 95adcea8434b661282a0aba21dbe53c6d47f2e94
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
<<<<<<< HEAD
=======
    }

    public EmployeeResponse getEmployeeByDni(String dni)
    {
        return employeeRepository.findByDni(dni);
>>>>>>> 95adcea8434b661282a0aba21dbe53c6d47f2e94
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

<<<<<<< HEAD

    public Status toggleStatus(Long id) {
        // Recupera o lanza excepción si no existe
=======
    public Status toggleStatus(Long id) {
>>>>>>> 95adcea8434b661282a0aba21dbe53c6d47f2e94
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
