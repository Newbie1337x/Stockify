package org.stockify.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.Model.Entities.ProviderEntity;

@Repository
public interface ProviderRepo extends JpaRepository<ProviderEntity, Long> {

}
