package org.stockify.security.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockify.security.model.entity.CredentialsEntity;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialsEntity, Long> {

    Optional<CredentialsEntity> findByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("""
    SELECT c FROM CredentialsEntity c
    JOIN FETCH c.roles r
    JOIN FETCH r.permits
    WHERE c.username = :username
    """)
    Optional<CredentialsEntity> findByUsernameWithRolesAndPermits(@Param("username") String username);
}
