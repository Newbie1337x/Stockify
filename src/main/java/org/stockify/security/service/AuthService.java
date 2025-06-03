package org.stockify.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.stockify.security.repository.CredentialRepository;
import org.stockify.security.model.dto.request.AuthRequest;

@Service
public class AuthService {

    private final CredentialRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    public AuthService(CredentialRepository credentialsRepository,
                       AuthenticationManager authenticationManager) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;
    }
    public UserDetails authenticate(AuthRequest input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.username(),
                            input.password()
                    )
            );
            return credentialsRepository.findByUsername(input.username())
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found with email: " + input.username()));
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid username or password");
        } catch (Exception e) {
            throw new org.stockify.security.exception.AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }
}
