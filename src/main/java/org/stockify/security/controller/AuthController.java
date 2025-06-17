package org.stockify.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.stockify.security.model.dto.request.AuthRequest;
import org.stockify.security.model.dto.response.AuthResponse;
import org.stockify.security.service.AuthService;
import org.stockify.security.service.JwtService;

/**
 * REST controller for authentication operations.
 * Provides endpoints for user login and logout.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations for user authentication")
public class AuthController {

    /**
     * Service for authentication operations
     */
    private final AuthService authService;

    /**
     * Service for JWT token operations
     */
    private final JwtService jwtService;

    /**
     * Constructor for AuthController
     * 
     * @param authService Service for authentication operations
     * @param jwtService Service for JWT token operations
     */
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * Logs out the currently authenticated user
     *
     * @return A success message
     */
    @PostMapping("/logout")
    @Operation(
        summary = "Logout the current user", 
        description = "Invalidates the current user's session and JWT token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Logout successful");
    }


    /**
     * Authenticates a user and generates a JWT token
     *
     * @param authRequest Request containing username and password
     * @return Response containing the JWT token
     */
    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user and get token",
        description = "Validates user credentials and returns a JWT token for authenticated requests"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Authentication credentials") @RequestBody AuthRequest authRequest) {
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token)); 
    }


}
