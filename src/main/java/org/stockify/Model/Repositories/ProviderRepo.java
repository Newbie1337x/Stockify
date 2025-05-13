package org.stockify.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.stockify.Model.Entities.ProviderEntity;
import java.util.List;

@Repository
public interface ProviderRepo extends JpaRepository<ProviderEntity, Long>, PagingAndSortingRepository<ProviderEntity,Long> {

    List<ProviderEntity> findByName(String name);

    ProviderEntity findByRazonSocial(String razonSocial);

    ProviderEntity findByCuit(String cuit);

    ProviderEntity findByMail(String mail);
}
