package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public void createEmployee(EmployeeEntity employeeEntity) {
        employeeRepository.save(employeeEntity);
    }
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.getReferenceById(id);
    }
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
    public void updateEmployee(EmployeeEntity employeeEntity) {
        employeeRepository.save(employeeEntity);
    }
}
