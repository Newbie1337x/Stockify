package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.assembler.EmployeeModelAssembler;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.service.EmployeeService;

/**
 * REST controller for managing employees.
 * Provides endpoints for CRUD operations on employees.
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Operations for managing employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    /**
     * Service for employee operations
     */
    private final EmployeeService employeeService;

    /**
     * Assembler for converting employee entities to HATEOAS models
     */
    private final EmployeeModelAssembler employeeModelAssembler;

    /**
     * Mapper for converting between employee entities and DTOs
     */
    private final EmployeeMapper employeeMapper;

    @Operation(summary = "List all employees with optional filters")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully")
    })
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('READ') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getAllEmployees(
            @Parameter(description = "Filter by employee name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by employee last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter by employee DNI") @RequestParam(required = false) String dni,
            @Parameter(hidden = true) Pageable pageable,
            PagedResourcesAssembler<EmployeeResponse> pagedAssembler
    ) {
        Page<EmployeeResponse> employeePage = employeeService.getEmployees(name, lastName, dni, pageable);
        PagedModel<EntityModel<EmployeeResponse>> model = pagedAssembler.toModel(employeePage, employeeModelAssembler);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/admin/{employeeID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('READ') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<EmployeeResponse>> getEmployeeById(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID) {
        EmployeeResponse employee = employeeService.getEmployeeById(employeeID);
        return ResponseEntity.ok(employeeModelAssembler.toModel(employee));
    }

    /**
     * Retrieves the profile of the currently authenticated employee
     *
     * @param authentication The authentication object containing the current user's credentials
     * @return The employee profile information
     */
    @Operation(summary = "Get current employee profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Employee profile not found")
    })
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<EntityModel<EmployeeResponse>> getProfile(
            @Parameter(description = "Authentication object with user credentials", hidden = true) Authentication authentication) {

        EmployeeEntity employee = employeeService.getProfile(authentication);

        return ResponseEntity.ok(employeeModelAssembler.toModel(employeeMapper.toResponseDto(employee)));
    }


    @Operation(summary = "Logically disable an employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee logically disabled successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PatchMapping("/{employeeID}/disable")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<Void> disableEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID) {
        employeeService.delete(employeeID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Logically reactivate an employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee reactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PatchMapping("/{employeeID}/enable")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<EmployeeResponse>> enableEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID) {
        EmployeeResponse employee = employeeService.reactivate(employeeID);
        return ResponseEntity.ok(employeeModelAssembler.toModel(employee));
    }

    /**
     * Fully updates an employee with the provided information
     *
     * @param employeeID ID of the employee to update
     * @param employeeRequest Request body containing the updated employee information
     * @return The updated employee entity
     */
    @Operation(summary = "Fully update an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{employeeID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EmployeeEntity> putEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID,
            @Parameter(description = "Updated employee information") @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.ok(employeeEntity);
    }

    /**
     * Partially updates an employee with the provided information
     *
     * @param employeeID ID of the employee to update
     * @param employeeRequest Request body containing the fields to update
     * @return The updated employee entity
     */
    @Operation(summary = "Partially update an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PatchMapping("/{employeeID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EmployeeEntity> patchEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeID,
            @Parameter(description = "Fields to update in the employee") @RequestBody EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeService.updateEmployee(employeeRequest, employeeID);
        return ResponseEntity.ok(employeeEntity);
    }
}
