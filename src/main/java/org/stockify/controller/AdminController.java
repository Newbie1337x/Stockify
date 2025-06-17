package org.stockify.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.service.EmployeeService;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
import org.stockify.security.service.AuthService;

@RestController
@RequestMapping("/admin")
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
