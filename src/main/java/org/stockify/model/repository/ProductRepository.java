package org.stockify.model.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT p.categories FROM ProductEntity p WHERE p.id = :prodID")
    Page<CategoryEntity> findCategoriesByProductId(@Param("prodID") Long prodID, Pageable pageable);

    Page<ProductEntity> findAllByProviders_Id(Long providerId, Pageable pageable);



}
