package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
import org.stockify.security.service.AuthService;

/**
 * Controller for administrative operations.
 * Provides endpoints for managing user roles and permissions.
 */
@RestController
@RequestMapping("/admin")

@Tag(name = "Administration", description = "Operations for system administration and user management")


  
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Updates the roles and permissions for a user identified by email.
     *
     * @param email Email of the user to update
     * @param roleAndPermitsDTO DTO containing the roles and permissions to assign
     * @return Response containing the updated employee information
     */
    @Operation(summary = "Update user roles and permissions", 
               description = "Assigns new roles and permissions to an existing user identified by their email address")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles and permissions updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the provided email"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions to perform this operation"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PatchMapping("/update-role/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') AND hasAuthority('WRITE')")
    public ResponseEntity<EmployeeResponse> setPermitsOrRole(
            @Parameter(description = "Email address of the user to update", required = true) 
            @PathVariable String email, 

            @Parameter(description = "Roles and permissions to assign to the user", required = true)
            @RequestBody RoleAndPermitsDTO roleAndPermitsDTO) {
        return authService.setPermitsAndRole(email, roleAndPermitsDTO);
    }



}
