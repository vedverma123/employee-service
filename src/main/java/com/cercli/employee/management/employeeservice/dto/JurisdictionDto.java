package com.cercli.employee.management.employeeservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JurisdictionDto {
    @NotBlank
    String code;
    @NotBlank
    String name;
    @NotBlank
    String zoneId;
    @NotBlank
    String locale;
}
