package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.SessionPosEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//repository
@Repository
public interface SessionPosRepository extends JpaRepository<SessionPosEntity,Long>, JpaSpecificationExecutor<SessionPosEntity> {

   Optional<SessionPosEntity> findByPosEntity_IdAndCloseTime(Long posEntityId, LocalDateTime closeTime);

    Boolean existsByPosEntity_IdAndCloseTime(Long posEntityId, LocalDateTime closeTime);
}
