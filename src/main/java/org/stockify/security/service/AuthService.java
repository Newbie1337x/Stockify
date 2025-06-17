package org.stockify.security.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.employee.EmployeeRequest;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.mapper.CredentialMapper;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.security.exception.AuthenticationException;
import org.stockify.security.model.dto.request.CredentialRequest;
import org.stockify.security.model.dto.request.RegisterEmployeeRequest;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
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
@Transactional
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
        if (credentialsRepository.existsByEmail(registerEmployeeRequest.getCredential().getEmail())) {
            throw new DuplicatedUniqueConstraintException("Email address already in use");
        }

        if (employeeRepository.getEmployeeEntityByDni(registerEmployeeRequest.getEmployee().getDni()).isPresent()) {
            throw new DuplicatedUniqueConstraintException("DNI is already in use");
        }

        EmployeeEntity employee = employeeMapper.toEntity(registerEmployeeRequest.getEmployee());
        employee.setStatus(Status.OFFLINE);
        employee.setActive(true);
        employeeRepository.save(employee);

        Role inputRole = registerEmployeeRequest.getCredential().getRoles();
        // Verifico si estan intentando crear un administrador en la ruta publica
        if (inputRole == Role.ADMIN) {
            throw new IllegalArgumentException("Administrators cannot be registered");
        }

        Set<Permit> requestedPermits = registerEmployeeRequest.getCredential().getPermits();
        Set<PermitEntity> permitEntities = new HashSet<>();

        for (Permit p : requestedPermits) {
            // Busco si el permiso existe, si no existe, lo creo y lo guardo
            PermitEntity permitEntity = permitRepository.findByPermit(p)
                    .orElseGet(() -> {
                        PermitEntity newPermit = PermitEntity.builder()
                                .permit(p)
                                .build();
                        return permitRepository.save(newPermit);
                    });
            permitEntities.add(permitEntity);
        }

        RoleEntity roleEntity = rolRepository.findByRole(inputRole)
                .orElseGet(() -> {
                    RoleEntity newRole = RoleEntity.builder()
                            .role(inputRole)
                            .permits(permitEntities)
                            .build();
                    return rolRepository.save(newRole);
                });

        CredentialsEntity credentials = credentialMapper.toEntity(registerEmployeeRequest.getCredential());
        credentials.setEmployee(employee);
        credentials.setPassword(passwordEncoder.encode(registerEmployeeRequest.getCredential().getPassword()));

        credentials.setRoles(Set.of(roleEntity));

        credentialsRepository.save(credentials);

        return new AuthResponse(jwtService.generateToken(credentials));
    }

    public ResponseEntity<EmployeeResponse> setPermitsAndRole(String email, RoleAndPermitsDTO roleAndPermitsDTO) {

        CredentialsEntity credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        Set<Permit> requestedPermits = roleAndPermitsDTO.getPermits();
        Set<PermitEntity> permitEntities = new HashSet<>();

        for (Permit permit : requestedPermits) {
            PermitEntity permitEntity = permitRepository.findByPermit(permit)
                    .orElseGet(() -> permitRepository.save(
                            PermitEntity.builder().permit(permit).build()
                    ));
            permitEntities.add(permitEntity);
        }

        Role requestedRole = roleAndPermitsDTO.getRoles();
        RoleEntity roleEntity = rolRepository.findByRole(requestedRole)
                .orElseGet(() -> rolRepository.save(
                        RoleEntity.builder()
                                .role(requestedRole)
                                .permits(permitEntities)
                                .build()
                ));

        // Si el rol ya existía pero no tiene estos permisos, actualizarlos:
        if (!roleEntity.getPermits().containsAll(permitEntities)) {
            roleEntity.getPermits().addAll(permitEntities);
            rolRepository.save(roleEntity);
        }

        Set<RoleEntity> rolesActuales = credentials.getRoles();

        if (rolesActuales == null) {
            rolesActuales = new HashSet<>();
        } else {
            // En caso que sea un Set inmutable, lo hacemos mutable
            rolesActuales = new HashSet<>(rolesActuales);
        }

        rolesActuales.add(roleEntity);
        credentials.setRoles(rolesActuales);
        credentialsRepository.save(credentials);

        // 5. Devolver la respuesta del empleado
        EmployeeResponse response = employeeMapper.toResponseDto(credentials.getEmployee());
        return ResponseEntity.ok(response);
    }

    private List<PermitEntity> permitEmployee(){
        List<PermitEntity> permits = new ArrayList<>();

        // Get or create READ permit
        PermitEntity readPermit = permitRepository.findByPermit(Permit.READ)
                .orElseGet(() -> {
                    PermitEntity newPermit = PermitEntity.builder().permit(Permit.READ).build();
                    return permitRepository.save(newPermit);
                });
        permits.add(readPermit);

        // Get or create WRITE permit
        PermitEntity writePermit = permitRepository.findByPermit(Permit.WRITE)
                .orElseGet(() -> {
                    PermitEntity newPermit = PermitEntity.builder().permit(Permit.WRITE).build();
                    return permitRepository.save(newPermit);
                });
        permits.add(writePermit);

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

            CredentialsEntity credentials = credentialsRepository.findByEmail(input.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + input.email()));


            EmployeeEntity employee = credentials.getEmployee();
            if (employee != null) {
                if (employee.getStatus() == Status.ONLINE) {
                    throw new AuthenticationException("User is already logged in", null);
                }
                employee.setStatus(Status.ONLINE);
                employeeRepository.save(employee); // Guardás el cambio
            }

            return credentials;
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new org.stockify.security.exception.AuthenticationException("Invalid email or password", e);
        } catch (Exception e) {
            throw new org.stockify.security.exception.AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }

    public void logout(String email) {
        CredentialsEntity credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        EmployeeEntity employee = credentials.getEmployee();
        if (employee != null) {
            employee.setStatus(Status.OFFLINE);
            employeeRepository.save(employee);
        }
    }


}
