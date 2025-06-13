package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.assembler.EmployeeModelAssembler;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.service.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeService = employeeService;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String dni,
            Pageable pageable,
            PagedResourcesAssembler<EmployeeResponse> pagedAssembler
    ) {
        Page<EmployeeResponse> employeePage = employeeService.getEmployees(id, name, lastName, dni, pageable);
        PagedModel<EntityModel<EmployeeResponse>> model = pagedAssembler.toModel(employeePage, employeeModelAssembler);
        return ResponseEntity.ok(model);
    }


    @GetMapping("/{employeeID}")
    public ResponseEntity<EntityModel<EmployeeResponse>> getEmployeeById(@PathVariable Long employeeID) {
        EmployeeResponse employee = employeeService.getEmployeeById(employeeID);
        return ResponseEntity.ok(employeeModelAssembler.toModel(employee));
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(
            @Validated @RequestBody EmployeeRequest employeeRequest
    ){
        return ResponseEntity.status(201).body(employeeService.createEmployee(employeeRequest));
    }

    @DeleteMapping("/{employeeID}")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long employeeID) {
        employeeService.delete(employeeID);
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/{employeeID}")
    public ResponseEntity<EmployeeEntity> putEmployee(@PathVariable Long employeeID, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.status(200).body(employeeEntity);
    }

    @PatchMapping("/{employeeID}")
    public ResponseEntity<EmployeeEntity> patchEmployee(@PathVariable Long employeeID, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.status(200).body(employeeEntity);
    }
}
