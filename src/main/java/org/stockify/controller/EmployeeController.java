package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

//    @Deprecated
//    @GetMapping
//    public List<EmployeeResponse> getEmployees(@RequestParam(required = false) String name, @RequestParam(required = false) Long id, @RequestParam(required = false) String lastName) throws EmployeeNotFoundException {
//       List<EmployeeResponse> employees = new ArrayList<>();
//
//        if (id != null) {
//            employees.add(employeeService.getEmployeeById(id));
//            return employees;
//        }
//        else if (name != null) {
//            employees = employeeService.getEmployeeByName(name);
//            return employees;
//        }
//       else if (lastName != null) {
//            employees = employeeService.getEmployeeByLastName(lastName);
//            return employees;
//        }     else {     return employeeService.getAllEmplyeesActive();
//       }
//   }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employee = employeeService.getAllEmplyeesActive();
        return new ResponseEntity<>(employee, HttpStatusCode.valueOf(200));
    }



}
