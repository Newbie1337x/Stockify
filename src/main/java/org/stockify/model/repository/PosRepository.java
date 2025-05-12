package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;

import java.util.List;

@Repository
public interface PosRepository extends JpaRepository<PosEntity,Long> {


    List<PosEntity> findByStatus(Status status);
}
