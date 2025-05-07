package org.stockify.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entities.ClientEntity;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    List<ClientEntity> findByFirstName(String firstName);
}
