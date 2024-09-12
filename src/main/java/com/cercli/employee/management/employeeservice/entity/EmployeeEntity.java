package com.cercli.employee.management.employeeservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    UUID employeeId;

    @ManyToOne
    @JoinColumn(name = "jurisdiction_code")
    JurisdictionEntity jurisdiction;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String position;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    BigDecimal salary;

    @Column(nullable = false)
    ZonedDateTime createdAt;

    @Column(nullable = false)
    ZonedDateTime modifiedAt;

}
