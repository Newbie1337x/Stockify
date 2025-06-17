package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.security.model.dto.request.RegisterEmployeeRequest;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
import org.stockify.security.model.enums.Role;
import org.stockify.security.service.AuthService;

/**
 * Controller for user management operations.
 * Provides endpoints for managing users, their roles and permissions.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations for managing users, their roles and permissions")
public class UserManagementController {

    private final AuthService authService;

    public UserManagementController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new employee in the system
     *
     * @param registerEmployeeRequest Request containing employee and credential information
     * @return The created employee information
     */
    @PostMapping("/register/employee")
    @Operation(
        summary = "Register a new employee", 
        description = "Creates a new employee account with regular employee permissions",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "409", description = "Employee with the same DNI already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeResponse> registerEmployee(
            @Parameter(description = "Employee registration information") @RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        EmployeeResponse response = authService.registerEmployee(registerEmployeeRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new administrator in the system
     *
     * @param registerEmployeeRequest Request containing employee and credential information
     * @return They created admin employee information
     */
    @PostMapping("/register/admin")
    @Operation(
        summary = "Register a new administrator", 
        description = "Creates a new employee account with administrator permissions",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Administrator registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "409", description = "Employee with the same DNI already exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> registerAdmin(
            @Parameter(description = "Administrator registration information") @RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        registerEmployeeRequest.getCredential().setRoles(Role.ADMIN);
        EmployeeResponse response = authService.registerEmployee(registerEmployeeRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the roles and permissions for a user identified by email.
     *
     * @param email Email of the user to update
     * @param roleAndPermitsDTO DTO containing the roles and permissions to assign
     * @return Response containing the updated employee information
     */
    @PatchMapping("/update-role/{email}")
    @Operation(summary = "Update user roles and permissions", 
               description = "Assigns new roles and permissions to an existing user identified by their email address",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles and permissions updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the provided email"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions to perform this operation"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') AND hasAuthority('WRITE')")
    public ResponseEntity<EmployeeResponse> setPermitsOrRole(
            @Parameter(description = "Email address of the user to update", required = true) 
            @PathVariable String email, 
            @Parameter(description = "Roles and permissions to assign to the user", required = true)
            @RequestBody RoleAndPermitsDTO roleAndPermitsDTO) {
        return authService.setPermitsAndRole(email, roleAndPermitsDTO);
    }
}