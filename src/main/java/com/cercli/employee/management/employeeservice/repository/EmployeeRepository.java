package com.cercli.employee.management.employeeservice.repository;

import com.cercli.employee.management.employeeservice.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
}
