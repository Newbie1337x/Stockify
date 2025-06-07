package org.stockify.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockify.security.model.dto.request.AuthRequest;
import org.stockify.security.model.dto.response.AuthResponse;
import org.stockify.security.service.AuthService;
import org.stockify.security.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService
            jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody
                                                         AuthRequest authRequest){

        System.out.println(authRequest);
        UserDetails user = authService.authenticate(authRequest);
        System.out.println(user);
        String token = jwtService.generateToken(user);
        System.out.println(token);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}