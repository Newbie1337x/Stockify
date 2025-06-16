package org.stockify.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.security.model.dto.request.AuthRequest;
import org.stockify.security.model.dto.request.CredentialRequest;
import org.stockify.security.model.dto.request.RegisterEmployeeRequest;
import org.stockify.security.model.dto.response.AuthResponse;
import org.stockify.security.service.AuthService;
import org.stockify.security.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService
            jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/employee")
    public ResponseEntity<AuthResponse> registerEmployee(
            @RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        AuthResponse response = authService.registerEmployee(registerEmployeeRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cerrar_sesion")
    public ResponseEntity<String> cerrarSesion(@RequestHeader("Authorization") String token){
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Se ha cerrado el sesion");
    }

    @PostMapping("/iniciar_sesion")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody
                                                         AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}