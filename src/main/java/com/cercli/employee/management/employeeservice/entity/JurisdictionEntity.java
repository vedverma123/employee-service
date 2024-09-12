package com.cercli.employee.management.employeeservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "jurisdiction")
public class JurisdictionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String zoneId;

    @Column(nullable = false)
    String locale;

    @Override
    public String toString() {
        return "Jurisdiction{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
