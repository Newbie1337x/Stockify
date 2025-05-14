package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ProviderEntity;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {

    Page<ProviderEntity> findByName(Pageable pageable, String name);

    ProviderEntity findByBusinessName(String businessName);

    ProviderEntity findByTaxId(String taxId);

    ProviderEntity findByEmail(String email);


}