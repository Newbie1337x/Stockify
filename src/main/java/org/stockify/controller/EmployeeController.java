package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.EmployeeRequest;
import org.stockify.model.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
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

//    @GetMapping
//    public List<EmployeeResponse> getEmployees(@RequestParam(required = false) String name, @RequestParam(required = false) Long id, @RequestParam(required = false) String lastName) throws EmployeeNotFoundException {
//        List<EmployeeResponse> employees = new ArrayList<>();
//
//        if (id != null) {
//            employees.add(employeeService.getEmployeeById(id));
//            return employees;
//        }
//        else if (name != null) {
//            employees = employeeService.getEmployeeByName(name);
//            return employees;
//        }
//        else if (lastName != null) {
//            employees = employeeService.getEmployeeByLastName(lastName);
//            return employees;
//        }
//        else {
//            return employeeService.getAllEmplyeesActive();
//        }
//    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeesActive() {
        List<EmployeeResponse> employee = employeeService.getAllEmplyeesActive();
        return new ResponseEntity<>(employee, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employee = employeeService.getAllEmployees();
        return new ResponseEntity<>(employee, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<EmployeeResponse> getEmployeeById(@RequestParam Long id) {
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employeeResponse, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployeeByName(@RequestParam String name) {
        List<EmployeeResponse> employee = employeeService.getEmployeeByName(name);
        return ResponseEntity.status(200).body(employee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployeeByLastName(@RequestParam String lastName) {
        List<EmployeeResponse> employee = employeeService.getEmployeeByLastName(lastName);
        return ResponseEntity.status(200).body(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(@Validated @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.status(201).body(employeeService.createEmployee(employeeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.status(200).body(null);
    }




}
