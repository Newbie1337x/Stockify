package org.stockify.model.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entities.ProviderEntity;
import java.util.List;

@Repository
public interface ProviderRepo extends JpaRepository<ProviderEntity, Long>, PagingAndSortingRepository<ProviderEntity,Long> {

    List<ProviderEntity> findByName(String name);

    ProviderEntity findByRazonSocial(String razonSocial);

    ProviderEntity findByCuit(String cuit);

    ProviderEntity findByMail(String mail);

    Page<ProviderEntity> findAllByActivo(Pageable pageable);

}
