package org.stockify.security.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.mapper.CredentialMapper;
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.repository.EmployeeRepository;
import org.stockify.model.service.PosService;
import org.stockify.security.exception.AuthenticationException;
import org.stockify.security.model.dto.request.RegisterEmployeeRequest;
import org.stockify.security.model.dto.request.RoleAndPermitsDTO;
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

/**
 * Service responsible for authentication and user management operations.
 * Handles user registration, authentication, role and permission management.
 */
@Transactional
@Service
public class AuthService {

    /**
     * Repository for user credentials
     */
    private final CredentialRepository credentialsRepository;

    /**
     * Spring Security authentication manager
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Password encoder for secure password storage
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Repository for employee data
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Mapper for converting between employee entities and DTOs
     */
    private final EmployeeMapper employeeMapper;

    /**
     * Mapper for converting between credential entities and DTOs
     */
    private final CredentialMapper credentialMapper;

    /**
     * Service for JWT token operations
     */
    private final JwtService jwtService;

    /**
     * Repository for permission data
     */
    private final PermitRepository permitRepository;

    /**
     * Repository for role data
     */
    private final RolRepository rolRepository;

    /**
     * Service for Point of Sale operations
     */
    private final PosService posService;

    /**
     * Constructor for AuthService
     *
     * @param credentialsRepository Repository for user credentials
     * @param authenticationManager Spring Security authentication manager
     * @param passwordEncoder Password encoder for secure password storage
     * @param employeeRepository Repository for employee data
     * @param employeeMapper Mapper for converting between employee entities and DTOs
     * @param credentialMapper Mapper for converting between credential entities and DTOs
     * @param jwtService Service for JWT token operations
     * @param permitRepository Repository for permission data
     * @param rolRepository Repository for role data
     * @param posService Service for Point of Sale operations
     */
    public AuthService(CredentialRepository credentialsRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       EmployeeRepository employeeRepository,
                       EmployeeMapper employeeMapper,
                       CredentialMapper credentialMapper,
                       JwtService jwtService,
                       PermitRepository permitRepository,
                       RolRepository rolRepository, PosService posService) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.credentialMapper = credentialMapper;
        this.jwtService = jwtService;
        this.permitRepository = permitRepository;
        this.rolRepository = rolRepository;
        this.posService = posService;
    }

    /**
     * Registers a new employee in the system with the specified role and permissions.
     * Validates that the email and DNI are not already in use.
     * Ensures that only admins can create admin accounts.
     *
     * @param registerEmployeeRequest Request containing employee and credential information
     * @return The created employee information
     * @throws DuplicatedUniqueConstraintException If email or DNI is already in use
     * @throws AuthenticationException If a manager tries to create an admin account
     */
    public EmployeeResponse registerEmployee(RegisterEmployeeRequest registerEmployeeRequest) {
        // Access is already restricted by @PreAuthorize annotation in the controller

        // Get current user's authorities to check if they are admin or manager
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isManager = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));

        // Check if email or DNI is already in use
        if (credentialsRepository.existsByEmail(registerEmployeeRequest.getCredential().getEmail())) {
            throw new DuplicatedUniqueConstraintException("Email address already in use");
        }

        if (employeeRepository.getEmployeeEntityByDni(registerEmployeeRequest.getEmployee().getDni()).isPresent()) {
            throw new DuplicatedUniqueConstraintException("DNI is already in use");
        }

        Role inputRole = registerEmployeeRequest.getCredential().getRoles();

        // If the current user is a MANAGER and trying to create an ADMIN account, throw an exception
        if (isManager && !isAdmin && inputRole == Role.ADMIN) {
            throw new AuthenticationException("MANAGER role cannot create ADMIN accounts", null);
        }

        // Create the employee entity
        EmployeeEntity employee = employeeMapper.toEntity(registerEmployeeRequest.getEmployee());
        employee.setStatus(Status.OFFLINE);
        employee.setActive(true);
        employeeRepository.save(employee);

        // Process permissions
        Set<Permit> requestedPermits = registerEmployeeRequest.getCredential().getPermits();
        Set<PermitEntity> permitEntities = new HashSet<>();

        for (Permit p : requestedPermits) {
            // Find or create the permission
            PermitEntity permitEntity = permitRepository.findByPermit(p)
                    .orElseGet(() -> {
                        PermitEntity newPermit = PermitEntity.builder()
                                .permit(p)
                                .build();
                        return permitRepository.save(newPermit);
                    });
            permitEntities.add(permitEntity);
        }

        // Find or create the role
        RoleEntity roleEntity = rolRepository.findByRole(inputRole)
                .orElseGet(() -> {
                    RoleEntity newRole = RoleEntity.builder()
                            .role(inputRole)
                            .permits(permitEntities)
                            .build();
                    return rolRepository.save(newRole);
                });

        // Create the credentials
        CredentialsEntity credentials = credentialMapper.toEntity(registerEmployeeRequest.getCredential());
        credentials.setEmployee(employee);
        credentials.setPassword(passwordEncoder.encode(registerEmployeeRequest.getCredential().getPassword()));
        credentials.setRoles(Set.of(roleEntity));
        credentialsRepository.save(credentials);

        // Return the employee response without generating a JWT token
        return employeeMapper.toResponseDto(employee);
    }

    /**
     * Updates the roles and permissions for a user identified by email.
     * If the requested role or permissions don't exist, they will be created.
     *
     * @param email Email of the user to update
     * @param roleAndPermitsDTO DTO containing the roles and permissions to assign
     * @return Response containing the updated employee information
     * @throws UsernameNotFoundException If no user is found with the provided email
     */
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

    /**
     * Creates or retrieves the standard set of permissions for regular employees.
     * This includes READ and WRITE permissions.
     *
     * @return List of permission entities for regular employees
     */
    public List<PermitEntity> permitEmployee(){
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

    /**
     * Authenticates a user with the provided credentials.
     * If authentication is successful, updates the employee status to ONLINE.
     *
     * @param input Authentication request containing email and password
     * @return UserDetails of the authenticated user
     * @throws AuthenticationException If authentication fails or user is already logged in
     * @throws UsernameNotFoundException If no user is found with the provided email
     */
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
                employeeRepository.save(employee);
            }

            return credentials;
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid email or password", e);
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Logs out the currently authenticated user.
     * Updates the employee status to OFFLINE and invalidates the JWT token.
     * Checks if the employee has an open POS session before allowing logout.
     *
     * @throws UsernameNotFoundException If no user is found with the extracted email
     * @throws InvalidSessionStatusException If the employee has an open POS session
     */
    public void logout() {
        String token = jwtService.extractTokenFromSecurityContext();
        String email = jwtService.extractUsername(token);
        CredentialsEntity credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        EmployeeEntity employee = credentials.getEmployee();
        if (employee != null) {
            if (!posService.itOpenedByEmployeeId(employee.getId()))
            {
                throw new InvalidSessionStatusException("The POS is opened To be able to close it, you first need to end the session.");
            }
            employee.setStatus(Status.OFFLINE);
            employeeRepository.save(employee);
        }
        jwtService.invalidateToken(token);
    }



}
