package org.stockify.security.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.security.model.entity.CredentialsEntity;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialsEntity, Long> {

    Optional<CredentialsEntity> findByUsername(String username);
}
