package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.SessionPosEntity;

@Repository
public interface SessionPosRepository extends JpaRepository<SessionPosEntity,Long> {

}
