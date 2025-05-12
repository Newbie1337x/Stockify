package org.stockify.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.stockify.Model.Entities.ProviderEntity;

@Repository
public interface ProviderRepo extends JpaRepository<ProviderEntity, Long>, PagingAndSortingRepository<ProviderEntity,Long> {

    ProviderEntity findByName(String name);

    ProviderEntity findByEmail(String email);

    ProviderEntity findByRazonSocial(String razonSocial);

    ProviderEntity findByCUIT(String cuit);

    ProviderEntity findByDireccionFiscal(String direccionFiscal);
}
