package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.model.exception.EmployeeNotFoundException;
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public Boolean createEmployee(EmployeeEntity employeeEntity) {
        if (employeeEntity.getId() != null) {
            EmployeeEntity employee = employeeRepository.findById(employeeEntity.getId()).get();
            if(!employee.getActive()){
                employee.setActive(true);
                employeeRepository.save(employee);
                return true;
            }
            return false;
        }
        employeeRepository.save(employeeEntity);
        return true;
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

    public EmployeeResponse getEmployeeById(Long id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Empleado no encontrado con ID " + id));
    }

    public void delete(Long id) throws EmployeeNotFoundException {
       EmployeeEntity employeeEntity = employeeMapper.toEntity(getEmployeeById(id));
       employeeEntity.setActive(false);
       employeeRepository.save(employeeEntity);
    }
    public void updateEmployee(EmployeeRequest employeeEntity) {
        employeeRepository.save(employeeMapper.toEntity(employeeEntity));
    }
}
