package org.stockify.model.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entities.ClientEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {
    /// ESTO ESTÁ BIEN O NO HACE FALTA ?
    Page<ClientEntity> findByFirstName(String firstName, Pageable pageable);
    Optional<ClientEntity> findById(Long id);
    Page<ClientEntity> findAll(Pageable pageable);
}