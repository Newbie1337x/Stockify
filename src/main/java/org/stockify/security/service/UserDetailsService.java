package org.stockify.security.service;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stockify.security.repository.CredentialRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final CredentialRepository credentialsRepository;

    public UserDetailsService(CredentialRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return credentialsRepository.findByEmailWithRolesAndPermits(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
