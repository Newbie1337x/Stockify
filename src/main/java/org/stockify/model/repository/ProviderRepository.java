package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.ProviderEntity;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long>, JpaSpecificationExecutor<ProviderEntity> {

    Page<ProviderEntity> findAllByProductList_Id(Pageable pageable, @Param("prodID") Long prodID);

    /**
     * Find a provider by ID regardless of its active status.
     * This method bypasses the @Where clause by using a native SQL query.
     *
     * @param id the ID of the provider to find
     * @return an Optional containing the provider if found, or empty if not found
     */
    @Query(value = "SELECT * FROM providers WHERE provider_id = :id", nativeQuery = true)
    Optional<ProviderEntity> findByIdIncludingInactive(@Param("id") Long id);
}
