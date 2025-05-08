package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.PosEntity;

@Repository
public interface PosRepository extends JpaRepository<PosEntity,Long> {


}
