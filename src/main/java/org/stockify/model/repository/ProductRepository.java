package org.stockify.model.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT COUNT(p) > 0 FROM ProductEntity p WHERE LOWER(p.name) = LOWER(:name) AND p.id <> :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") int id);


    @Query("SELECT p.categories FROM ProductEntity p WHERE p.id = :prodID")
    Set<CategoryEntity> findCategoriesByProductId(@Param("prodID") int prodID);

    @Query("""
    SELECT p
    FROM ProductEntity p
    JOIN p.categories c
    WHERE LOWER(c.name) IN :names
    GROUP BY p.id
    HAVING COUNT(DISTINCT c.id) >= :size
""")
    Page<ProductEntity> findByAllCategoryNames(
            @Param("names") Set<String> names,
            @Param("size") long size
             ,Pageable pageable
    );


}
