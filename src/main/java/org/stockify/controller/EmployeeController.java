package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.assembler.EmployeeModelAssembler;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.service.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;

@RestController
@RequestMapping("/employee")
@Tag(name = "Employees", description = "Operations related to employee management")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeService = employeeService;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @Operation(
            summary = "List employees",
            description = "Retrieves a paginated list of employees with optional filters by ID, name, last name, and DNI.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String dni,
            @PageableDefault(size = 10) Pageable pageable,
            PagedResourcesAssembler<EmployeeResponse> pagedAssembler
    ) {
        Page<EmployeeResponse> employeePage = employeeService.getEmployees(id, name, lastName, dni, pageable);
        PagedModel<EntityModel<EmployeeResponse>> model = pagedAssembler.toModel(employeePage, employeeModelAssembler);
        return ResponseEntity.ok(model);
    }


    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves an employee by their unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Employee retrieved successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Employee not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeModelAssembler.toModel(employee));
    }

    @Operation(
            summary = "Create a new employee",
            description = "Registers a new employee in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Employee created successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(
            @Validated @RequestBody EmployeeRequest employeeRequest
    ){
        return ResponseEntity.status(201).body(employeeService.createEmployee(employeeRequest));
    }

    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Employee not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.status(200).body(null);
    }

    @Operation(
            summary = "Fully update an employee",
            description = "Updates all fields of an employee identified by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee updated successfully")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeEntity> putEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, id);
        return ResponseEntity.status(200).body(employeeEntity);
    }

    @Operation(
            summary = "Partially update an employee",
            description = "Updates one or more fields of an employee identified by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee partially updated successfully")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeEntity> patchEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, id);
        return ResponseEntity.ok(employeeEntity);
    }
}
