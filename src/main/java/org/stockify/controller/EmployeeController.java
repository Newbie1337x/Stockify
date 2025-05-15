package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.exception.EmployeeNotFoundException;
import org.stockify.model.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String dni
    ) {
        List<EmployeeResponse> employees = new ArrayList<>();
        employees = employeeService.getEmployee(id, name, lastName, dni);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(
            @Validated @RequestBody EmployeeRequest employeeRequest
    ){
        return ResponseEntity.status(201).body(employeeService.createEmployee(employeeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeEntity> putEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, id);
        return ResponseEntity.status(200).body(employeeEntity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeEntity> patchEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, id);
        return ResponseEntity.status(200).body(employeeEntity);
    }

}
