package com.cercli.employee.management.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EmployeeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID employeeId;

    @NotBlank(message = "Name must not be empty")
    String name;

    @NotBlank(message = "Position must not be empty")
    String position;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    String email;

    @Min(value = 1, message = "Salary must be greater than zero")
    BigDecimal salary;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    ZonedDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    ZonedDateTime modifiedAt;

    @NotBlank(message = "Jurisdiction must not be empty")
    String jurisdictionCode;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", jurisdictionCode='" + jurisdictionCode + '\'' +
                '}';
    }
}
