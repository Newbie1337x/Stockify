package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

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
    public List<EmployeeResponse> getAllEmployeesActive(){
        return employeeMapper.toResponseDtoList(employeeRepository.findAllActive());
    }
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeMapper.toResponseDto(employeeRepository.findById(id).get());
    }
    public void delete(Long id) {
       EmployeeEntity employeeEntity = employeeMapper.toEntity(getEmployeeById(id));
       employeeEntity.setActive(false);
       employeeRepository.save(employeeEntity);
    }
    public void updateEmployee(EmployeeRequest employeeEntity) {
        employeeRepository.save(employeeMapper.toEntity(employeeEntity));
    }
}
