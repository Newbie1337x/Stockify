package org.stockify.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>,
        JpaSpecificationExecutor<EmployeeEntity> {

    List<EmployeeEntity> findByStatus(Status status);
    Boolean existsByDni(String dni);
   Optional<EmployeeEntity> getEmployeeEntityByDni(String dni);

}
