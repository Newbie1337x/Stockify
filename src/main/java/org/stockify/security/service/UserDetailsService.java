package org.stockify.security.service;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialsRepository.findByUsernameWithRolesAndPermits(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
