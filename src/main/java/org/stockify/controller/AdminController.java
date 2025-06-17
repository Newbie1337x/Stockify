package org.stockify.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
import org.stockify.security.service.AuthService;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Operations for administrators")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PatchMapping("/update-role/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') AND hasAuthority('WRITE')")
    public ResponseEntity<EmployeeResponse> setPermitsOrRole(@PathVariable String email, @RequestBody RoleAndPermitsDTO roleAndPermitsDTO) {
        return authService.setPermitsAndRole(email, roleAndPermitsDTO);
    }



}
