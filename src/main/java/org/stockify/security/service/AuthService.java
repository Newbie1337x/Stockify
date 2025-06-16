package org.stockify.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.mapper.CredentialMapper;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.security.model.dto.request.CredentialRequest;
import org.stockify.security.model.dto.request.RegisterEmployeeRequest;
import org.stockify.security.model.dto.response.AuthResponse;
import org.stockify.security.model.entity.CredentialsEntity;
import org.stockify.security.model.entity.PermitEntity;
import org.stockify.security.model.entity.RoleEntity;
import org.stockify.security.model.enums.Permit;
import org.stockify.security.model.enums.Role;
import org.stockify.security.repository.CredentialRepository;
import org.stockify.security.model.dto.request.AuthRequest;
import org.stockify.security.repository.PermitRepository;
import org.stockify.security.repository.RolRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final CredentialRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final CredentialMapper credentialMapper;
    private final JwtService jwtService;
    private final PermitRepository permitRepository;
    private final RolRepository rolRepository;

    public AuthService(CredentialRepository credentialsRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       EmployeeRepository employeeRepository,
                       EmployeeMapper employeeMapper,
                       CredentialMapper credentialMapper,
                       JwtService jwtService,
                       PermitRepository permitRepository,
                       RolRepository rolRepository) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.credentialMapper = credentialMapper;
        this.jwtService = jwtService;
        this.permitRepository = permitRepository;
        this.rolRepository = rolRepository;
    }

    public AuthResponse registerEmployee(RegisterEmployeeRequest registerEmployeeRequest) {

        // Verificar si el email ya está registrado
        if (credentialsRepository.existsByEmail(registerEmployeeRequest.getCredential().getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        // Verificar si el DNI ya está registrado
        if (employeeRepository.getEmployeeEntityByDni(registerEmployeeRequest.getEmployee().getDni()).isPresent()) {
            throw new IllegalArgumentException("DNI is already in use");
        }

        // Mapear y guardar el empleado
        EmployeeEntity employee = employeeMapper.toEntity(registerEmployeeRequest.getEmployee());
        employee.setStatus(Status.OFFLINE);
        employee.setActive(true);
        employeeRepository.save(employee);

        // Obtener rol
        Role inputRole = registerEmployeeRequest.getCredential().getRoles();
        if (inputRole == Role.ADMIN) {
            throw new IllegalArgumentException("Administrators cannot be registered");
        }

        Set<Permit> requestedPermits = registerEmployeeRequest.getCredential().getPermits();
        Set<PermitEntity> permitEntities = new HashSet<>();

        for (Permit p : requestedPermits) {
            // Buscar el permiso existente, si no existe, crearlo y guardarlo
            PermitEntity permitEntity = permitRepository.findByPermit(p)
                    .orElseGet(() -> {
                        // Create a new PermitEntity if it doesn't exist
                        PermitEntity newPermit = PermitEntity.builder()
                                .permit(p) // Assuming PermitEntity has a 'permit' field
                                .build();
                        // Save the new permit to the database
                        return permitRepository.save(newPermit);
                    });
            permitEntities.add(permitEntity);
        }


        // Buscar o crear el RoleEntity con sus permisos
        RoleEntity roleEntity = rolRepository.findByRole(inputRole)
                .orElseGet(() -> {
                    RoleEntity newRole = RoleEntity.builder()
                            .role(inputRole)
                            .permits(permitEntities)
                            .build();
                    return rolRepository.save(newRole);
                });

        // Crear las credenciales
        CredentialsEntity credentials = credentialMapper.toEntity(registerEmployeeRequest.getCredential());
        credentials.setEmployee(employee);
        credentials.setPassword(passwordEncoder.encode(registerEmployeeRequest.getCredential().getPassword()));

        // Asignar el rol (único)
        credentials.setRoles(Set.of(roleEntity));

        // Guardar las credenciales
        credentialsRepository.save(credentials);

        // Generar token y devolver respuesta
        return new AuthResponse(jwtService.generateToken(credentials));
    }


    private List<PermitEntity> permitEmployee(){
        List<PermitEntity> permits = new ArrayList<>();
        PermitEntity permit = PermitEntity.builder().permit(Permit.READ).build();
        if (permitRepository.findByPermit(permit.getPermit()).isEmpty()){
            permitRepository.save(permit);
        }
        PermitEntity permit1 = PermitEntity.builder().permit(Permit.WRITE).build();
        if (permitRepository.findByPermit(permit1.getPermit()).isEmpty()){
            permitRepository.save(permit1);
        }
        permits.add(permit);
        permits.add(permit1);
        return permits;
    }

    public UserDetails authenticate(AuthRequest input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.email(),
                            input.password()
                    )
            );
            return credentialsRepository.findByEmail(input.email())
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found with email: " + input.email()));
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new org.stockify.security.exception.AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }


}
