package org.stockify.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.stockify.model.entity.EmployeeEntity;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>,
        JpaSpecificationExecutor<EmployeeEntity> {


   Optional<EmployeeEntity> getEmployeeEntityByDni(String dni);

}
