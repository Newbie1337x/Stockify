package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
=======
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
>>>>>>> 95adcea8434b661282a0aba21dbe53c6d47f2e94
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

<<<<<<< HEAD
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long id
    ){
        List<EmployeeResponse> employees = new ArrayList<>();
        employees = employeeService.getEmployee(id, name, lastName);
        return ResponseEntity.ok(employees);
=======

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employee = employeeService.getAllEmplyeesActive();
        return new ResponseEntity<>(employee, HttpStatusCode.valueOf(200));
>>>>>>> 95adcea8434b661282a0aba21dbe53c6d47f2e94
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
