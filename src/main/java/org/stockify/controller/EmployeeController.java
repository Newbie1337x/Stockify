package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.assembler.EmployeeModelAssembler;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Operations for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;

    @Operation(summary = "List all employees with optional filters")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully")
    })
    public ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getAllEmployees(
            @Parameter(description = "Filter by employee name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by employee last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter by employee ID") @RequestParam(required = false) Long id,
            @Parameter(description = "Filter by employee DNI") @RequestParam(required = false) String dni,
            @Parameter(hidden = true) Pageable pageable,
            PagedResourcesAssembler<EmployeeResponse> pagedAssembler
    ) {
        Page<EmployeeResponse> employeePage = employeeService.getEmployees(id, name, lastName, dni, pageable);
        PagedModel<EntityModel<EmployeeResponse>> model = pagedAssembler.toModel(employeePage, employeeModelAssembler);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{employeeID}")
    public ResponseEntity<EntityModel<EmployeeResponse>> getEmployeeById(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID) {
        EmployeeResponse employee = employeeService.getEmployeeById(employeeID);
        return ResponseEntity.ok(employeeModelAssembler.toModel(employee));
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.status(201).body(employeeService.createEmployee(employeeRequest));
    }

    @Operation(summary = "Delete an employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{employeeID}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID) {
        employeeService.delete(employeeID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Fully update an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{employeeID}")
    public ResponseEntity<EmployeeEntity> putEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID,
            @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.ok(employeeEntity);
    }

    @Operation(summary = "Partially update an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PatchMapping("/{employeeID}")
    public ResponseEntity<EmployeeEntity> patchEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID,
            @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.ok(employeeEntity);
    }
}
