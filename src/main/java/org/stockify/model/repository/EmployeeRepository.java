package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.enums.Status;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    List<EmployeeEntity> getEmployeeEntitiesByName(String name);
    List<EmployeeEntity> getEmployeeEntitiesByLastName(String lastName);
    List<EmployeeEntity> findByStatus(Status status);
}
